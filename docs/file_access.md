# File access (`READ`, `CHAIN`, `UPDATE`, ...) and how names are resolved

RPG file access looks simple in the source and is surprisingly hard to emulate. This document collects
the rules Jariko relies on, why they exist, and the bugs that taught us each of them, so that the next
person does not have to rediscover them from a stack trace.

## Why it is hard

- **Implicit state.** `READ`, `CHAIN`, `SETLL`, `UPDATE`, `DELETE` take no file handle. The runtime keeps
  a *current record* (and a position) per file, and `UPDATE`/`DELETE` act on it.
- **Two names for one thing.** A file has a *file name* (`C5C6M02L`) and its records have a *record
  format name* (`B£WKXTR`). Statements may be written with either, and the names can be changed from
  the program (`RENAME`, `PREFIX` on the F-spec) or from outside it (`OVRDBF`).
- **The platform resolves names, we rebuild them.** On IBM i the compiler and the database do it. In
  Jariko it is rebuilt at runtime from the F-specs and from the reload metadata (`RELOAD_METADATA/*.json`:
  `name`, `tableName`, `recordFormat`, keys).
- **Cursor semantics do not map 1:1 to SQL.** RPG assumes you can always update/delete the record you
  just read. With JDBC that depends on the engine and on the query (see "Reload side").

## How Jariko resolves a name

`DBFileMap` (`interpreter/DBFileMap.kt`) holds one `EnrichedDBFile` per F-spec, indexed twice:

| Index | Keys | Rule |
|---|---|---|
| `byFileName` | the F-spec file name | unique per F-spec |
| `byFormatName` | the F-spec's internal format name, and the record's native format name | first registration wins, with the exception below |

`get(name)` looks in `byFileName` first, then in `byFormatName`. Every statement goes through it
(`InterpreterCore.dbFile(name, statement)`), which also records the file as the *last used* one
(`status.lastDBFile`). In practice Jariko is lenient: any statement accepts either name.

### `RENAME` and the format-name collision

In RPG IV two files cannot use the same record format name in one program. When two files share a
format, one of them declares `RENAME(native:new)`. From then on the native name is **no longer that
file's format**: it can only mean the other file.

```
FC5C6M01L  UF A E  K DISK  USROPN RENAME(B£WKXTR:B£WKXT1)   <- B£WKXTR is now B£WKXT1 here
FC5C6M02L  UF A E  K DISK  USROPN                           <- B£WKXTR still means this file
...
C                   READ      C5C6M02L
C                   UPDATE    B£WKXTR                       <- must act on C5C6M02L
```

Jariko registers the native name for a renamed F-spec too, only as a **fallback**. The rule in
`DBFileMap.add` is therefore: the native name of a renamed F-spec never beats an F-spec that really
declares that name, whatever the declaration order. Everything else stays first-registration-wins
(for example an unkeyed F-spec declared before a renamed keyed one, see `ChainUnkeyedFormatDBTest`).

Before this rule existed, declaring the renamed F-spec first made `UPDATE B£WKXTR` resolve to
`C5C6M01L`, which had never read anything: `Positioning required before update`
(`UpdateRenamedFirstDBTest`).

Related details:
- An F-spec with no keys is treated as a Relative Record Number chain when addressed by format name
  (`ChainUnkeyedFormatDBTest`).
- `INFDS` is resolved per F-spec, never through the shared format alias (`InfdsRrnDBTest`).

## Reload side

Jariko talks to the database through `reload` (`SQLDBFile` in `reload/sql`). Things to know when a
file operation fails on a real database:

- `READ`/`CHAIN`/`SETLL` open a `ResultSet` with `CONCUR_UPDATABLE`. `UPDATE`/`DELETE` use it
  (`updateRow()`/`deleteRow()`), and fail with `Positioning required before update` when there is no
  open `ResultSet`, i.e. nothing was read through *that* file object.
- On DB2 for i a cursor whose query contains a `UNION` is **read-only** (`SQL0510`), even if updatable
  was requested. Keyed positioning queries are `UNION`s, so after a keyed read `deleteRow()` fails, and
  the failed call leaves the cursor in an error state (`SQL0906` on the next one). The DB2 dialect
  declares such cursors not updatable (`SQLDialect.isResultSetUpdatable`) and reload runs a searched
  `UPDATE`/`DELETE ... WHERE RRN(table) = ?` instead (reload PR smeup/reload#132; older reload versions
  fail with `SQL0510`). Engine-specific knowledge stays in `SQLDialect`.

## Writing tests for file access

`outputOfDBPgm(program, metadata, initialSQL, params)` (`db/utilities/dbTestUtils.kt`) runs an RPG
program against the **embedded in-memory HSQLDB** (`jdbc:hsqldb:mem:mainDb`, `TEST_DB_URL`), the same
setup reload uses. The database lives as long as the JVM, so drop or use unique table names.

Tips:
- Put the F-specs that collide on **different tables** when you need to see which file a statement
  hit (see `UPDATERENAMEDFIRST.rpgle`); in production they are usually logical files over one table.
- Declaration order matters: write the test with the order that used to break.
- Make sure the test fails without your fix (run it against the previous version of the main code).
- Do not use the in-process HSQLDB *server* mode for update tests: a positioned `updateRow()` made the
  server drop the connection (`EOFException`, then `Broken pipe` on close).

## Debugging checklist

1. Which F-specs does the program declare, in which order, with which `RENAME`/`PREFIX`? (RPG sources
   of the smeuperp app live under `~/dev/smeup-dsl/<lib>/JASRC`, searched in the order of
   `apps.<app>.libs` in `~/etc/kokos/apps.yaml`.)
2. In `RELOAD_METADATA/<FILE>.json`, which `tableName` and `recordFormat` do the files have?
3. Is the statement addressed by file name or by format name? By format name, which F-spec owns it?
4. Enable the `reload` logger at `TRACE` (`logback.xml`) and check which file object actually
   executed the last read and the failing statement.
5. If a cached AST is involved, remember to clear the pre-compiled `.bin` files and to make sure the
   running ME really uses the Jariko jar you built.
