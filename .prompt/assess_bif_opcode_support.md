# Prompt: Assess Support Level for Built-in Functions and Operation Codes

Use this prompt with Claude Code (or a similar coding LLM) from the root of the jariko repository to incrementally refresh `docs/built_in_functions.md` and `docs/operation_codes.md`.

---

## Task

Update `docs/built_in_functions.md` and `docs/operation_codes.md` to reflect the current implementation state of the JaRIKo interpreter. Perform an **incremental update**: only analyse changes introduced after the "Last refreshed" dates already recorded in those files.

## Step-by-step instructions

### 1. Read the current "Last refreshed" dates

Read both files:
- `docs/built_in_functions.md`
- `docs/operation_codes.md`

Look for a line of the form `_Last refreshed: YYYY-MM-DD_` near the top (or bottom) of each file.

- If no such line exists, **perform a full assessment**: skip the git-log filtering in step 2 and instead read the full current state of all relevant source files listed in that step. Treat every BIF and operation code as new — analyse all of them from scratch.
- If the line exists, treat its date as `BIF_DATE` / `OC_DATE` and proceed with the incremental git-log approach.

Record the resulting dates (or the sentinel "full assessment") as `BIF_DATE` and `OC_DATE`.

### 2. Find changed source files since each date

Run the following git commands and capture the output. These commands list files changed after the respective dates in the AST and interpreter packages:

```bash
# Files changed since BIF_DATE relevant to built-in functions
git log --since="<BIF_DATE>" --name-only --pretty=format: -- \
  rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/parsing/ast/builtin_functions.kt \
  rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/parsing/ast/expressions.kt \
  rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/interpreter/ExpressionEvaluation.kt \
  rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/interpreter/internal_interpreter.kt \
  | grep -v '^$' | sort -u

# Files changed since OC_DATE relevant to operation codes
git log --since="<OC_DATE>" --name-only --pretty=format: -- \
  rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/parsing/ast/statements.kt \
  rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/interpreter/internal_interpreter.kt \
  rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/interpreter/InterpreterCore.kt \
  | grep -v '^$' | sort -u
```

If `BIF_DATE` / `OC_DATE` is a "full assessment" sentinel (no prior `_Last refreshed_` line), read each source file in full and treat all classes as new — do not run the git-log commands.

If none of the files have changed since the recorded date, output `No changes detected since <date>` for that document and skip to step 5 to update the "Last refreshed" date to today.

### 3. Analyse implementation status

#### 3a. Operation codes — classes extending `Statement`

The canonical source of truth is:
```
rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/parsing/ast/statements.kt
```

Each concrete `data class` (or `class`) that extends `Statement` (directly or via an abstract intermediate) represents one or more RPG operation codes. The RPG opcode name is either:
- a `val opcode get() = "NAME"` override on the class, or
- derivable from the class name by stripping the `Stmt` suffix and upper-casing.

For each **new or modified** `Statement` subclass found in step 2, determine its support level:

| Level | Criteria |
|---|---|
| **Full** | The interpreter handles every documented variant; no `TODO(...)` calls or `throw NotImplementedError` in the execution path; tests exist in `src/test/`. |
| **Partial** | Some variants work but others have `TODO(...)`, throw on edge cases, or tests are missing for known scenarios. |
| **Not implemented** | The class exists in the AST but the interpreter has no `when` branch for it (falls to `else -> TODO()`) or immediately throws. |

Check the interpreter dispatch in:
- `rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/interpreter/internal_interpreter.kt`
- `rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/interpreter/InterpreterCore.kt`

**RPG domain knowledge check:** In addition to the code-evidence signals above, apply your knowledge of the IBM RPG ILE Language Reference to judge whether the implementation covers all meaningful use cases of the opcode. For each opcode, consider:

- **Operand combinations** — does the implementation handle all valid Factor 1 / Factor 2 / Result field combinations (e.g. `CHAIN` with and without a key list, `EVAL` with `*LIKE` length, `MOVE` between incompatible types)?
- **Resulting indicators** — are `%FOUND`, `%EOF`, `%EQUAL`, `%ERROR`, and Lo/Hi/Eq indicators set correctly in every path?
- **Edge cases known from the spec** — e.g. `DO` with a non-numeric limit, `FOR` with a negative step, `SELECT`/`WHEN` nesting, `CALL`/`CALLP` with omitted optional parameters, `MOVEL` with `*LIKE` padding.
Downgrade the level to **Partial** if your RPG knowledge reveals a plausible use case that the implementation clearly does not cover, even if no `TODO` marker is present.

#### 3b. Built-in functions — classes extending `Expression`

The canonical source of truth is:
```
rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/parsing/ast/builtin_functions.kt
```

(General expressions in `expressions.kt` are language primitives, not BIFs; ignore them.)

Each concrete `data class` / `class` that extends `Expression` in `builtin_functions.kt` corresponds to a `%BIF`. The RPG name is the class name with:
- `Expr` suffix stripped,
- a `%` prefix,
- upper-cased.

For example: `TrimExpr` → `%TRIM`, `SubstExpr` → `%SUBST`, `DecNumericExpr`/`DecTimeExpr` → `%DEC`.

Determine support level using the same criteria as for operation codes, but check the expression evaluator:
- `rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/interpreter/ExpressionEvaluation.kt`
- `rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/interpreter/internal_interpreter.kt`

**RPG domain knowledge check:** Apply your knowledge of the IBM RPG ILE Language Reference to judge whether the implementation covers all meaningful use cases of the BIF. For each BIF, consider:

- **Argument count and optionality** — e.g. `%SUBST(string:start)` vs. `%SUBST(string:start:length)`, `%SCAN` with and without a start position.
- **Type overloading** — some BIFs behave differently depending on the argument type (e.g. `%DEC` on a numeric vs. a character string, `%SIZE` on a field vs. an array, `%ELEM` on an array vs. a data structure).
- **Context sensitivity** — some BIFs are valid only in specific positions (e.g. `%SUBST` as an l-value in `EVAL`, `%PARMS` only inside a procedure).
- **Return type and precision** — e.g. `%CHAR` formatting rules for dates vs. numerics, `%EDITC`/`%EDITW` edit-word handling.
- **Error / boundary conditions** — e.g. `%SUBST` start > string length, `%SCAN` returning 0 when not found, `%SQRT` of a negative number.

Downgrade the level to **Partial** if your RPG knowledge reveals a plausible use case that the implementation clearly does not cover, even if no `TODO` marker is present.

### 4. Update the markdown tables

For each document:

- **Add new rows** for any operation code / BIF that is not yet listed.
- **Update existing rows** whose support level has changed; update the **Rationale** cell whenever you touch a row.
- **Do not remove rows** even if a class is deleted — mark them `Removed` and add a note in the Rationale cell.
- Keep the table sorted alphabetically by name.
- Every row must have a **Rationale** column. For rows you are adding or updating, fill it with a one-sentence summary of the evidence that drove the support level (e.g. `TODO in execute branch`, `no test for negative step`, `all documented variants covered and tested`). For rows you are not touching in this run, leave the existing content unchanged (or add a `-` if the cell is missing).
- Every row must have an **Example** column. For rows you are adding or updating:
  - **Partial**: provide a minimal inline RPG LE snippet (1–5 lines, using a fenced code block with ` ```rpgle `) that exposes the limitation and proves why the level is not Full (i.e. the snippet triggers the unimplemented or broken path).
  - **Full** or **Not implemented**: write `-`.
  For rows you are not touching in this run, leave the existing content unchanged (or add a `-` if the cell is missing).

Support level values to use in the table: `Full`, `Partial`, `Not implemented`.

Table column order: **Name | Support level | Rationale | Example**. If the existing table does not yet have a Rationale or Example column, add them as the last columns in that order.

### 5. Update (or add) the "Last refreshed" section

Add or update the following line near the **top** of each file, directly below the first `#` heading:

```
_Last refreshed: YYYY-MM-DD_
```

Use today's date (UTC).

### 6. Save

- Do **not** commit; just save the files. The human will review and commit.

## Important constraints

- Only look at git diff since the recorded date. Do not re-analyse the entire codebase from scratch.
- Do not upgrade a support level without code evidence (interpreter `when` branches, `TODO(...)` markers, test presence). Domain knowledge may be used to downgrade a level when a plausible use case is clearly not covered.
- Do not change the table structure beyond what is described above (adding the Rationale and Example columns are the permitted structural changes).
- Keep Rationale cells to one sentence; do not write multi-sentence prose inside table cells.
