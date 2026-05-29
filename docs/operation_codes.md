# Operation Codes

_Last refreshed: 2026-05-29_

In this document, we are going to list which **Operation Codes** are so far implemented in Jariko.

| Operation Code | Support level | Rationale |
| ------ | ------ | ------ |
| ADD | Full | Delegates to `add()` helper; result field serves as addend when Factor 1 is absent |
| ANDGE | Full | Handled via `continuedIndicators` mechanism on any statement |
| ANDGT | Full | Handled via `continuedIndicators` mechanism on any statement |
| ANDEQ | Full | Handled via `continuedIndicators` mechanism on any statement |
| ANDLE | Full | Handled via `continuedIndicators` mechanism on any statement |
| ANDLT | Full | Handled via `continuedIndicators` mechanism on any statement |
| ANDNE | Full | Handled via `continuedIndicators` mechanism on any statement |
| BEGSR | Full | Parse-time subroutine delimiter; no runtime execution needed |
| BITOFF | Not implemented | `MockStatement`; execute is a no-op; bit manipulation not actually performed |
| BITON | Not implemented | `MockStatement`; execute is a no-op; bit manipulation not actually performed |
| CABLE | Full | `CabStmt` computes comparison, sets HI/LO/EQ indicators, and throws `GotoException` when condition is met |
| CABEQ | Full | `CabStmt` computes comparison, sets HI/LO/EQ indicators, and throws `GotoException` when condition is met |
| CABGE | Full | `CabStmt` computes comparison, sets HI/LO/EQ indicators, and throws `GotoException` when condition is met |
| CABGT | Full | `CabStmt` computes comparison, sets HI/LO/EQ indicators, and throws `GotoException` when condition is met |
| CABLT | Full | `CabStmt` computes comparison, sets HI/LO/EQ indicators, and throws `GotoException` when condition is met |
| CABNE | Full | `CabStmt` computes comparison, sets HI/LO/EQ indicators, and throws `GotoException` when condition is met |
| CALL | Partial | Error indicator, parameter passing, and `callProgramHandler` all handled; `*OMIT` parameters not supported |
| CALLP | Full | Evaluates `FunctionCall` via `ExpressionEvaluation`; error indicator support present |
| CASE | Full | `CaseStmt` dispatches to correct subroutine via full comparison; OTHER subroutine falls to `other?.function` |
| CAT | Full | Handles Factor 1 absent (target as source), blanks-in-between, and `(P)` extender |
| CHAIN | Full | Delegates to `dbFile.chain(kList)`; key-list construction fully handled |
| CHECK | Full | Scans forward from start position; sets `lastFound` and result field for first character mismatch |
| CHECKR | Full | Scans backward from start position; sets `lastFound` and result field for first character mismatch |
| CLEAR | Full | Handles `DataRefExpr`, `IndicatorExpr`, and `ArrayAccessExpr`; throws for unknown target types |
| CLOSE | Partial | Sets `dbFile.open = false` for DB files; printer files have no corresponding dbFile entry |
| COMP | Full | Sets HI/LO/EQ indicators based on Factor 1 vs Factor 2 comparison |
| DEALLOC | Not implemented | `MockStatement`; execute is a no-op; memory deallocation not actually performed |
| DEFINE | Full | `DefineStmt` derives variable type from original name; `DefineDataAreaStmt` is declarative only |
| DELETE | Full | Builds `Record` from symbol table and delegates to `dbFile.delete(record)` |
| DIV | Full | Supports optional Factor 1, half-adjust extender, and MVR companion; divide-by-zero raises `ProgramStatusCode` |
| DO | Full | Supports index variable, start and end limits; handles ITER and LEAVE exceptions |
| DOU | Full | Executes body at least once; exits when end expression is true; handles LEAVE exception |
| DOUxx | Full | Old-form do-until with explicit comparison operator (DOUEQ, DOUNE, etc.); all variants handled |
| DOW | Full | Executes body while end expression is true; handles LEAVE exception |
| DOWxx | Full | Old-form do-while with explicit comparison operator (DOWEQ, DOWNE, etc.); all variants handled |
| DSPLY | Partial | Sends output to `systemInterface.display()`; response-input `TODO` not implemented |
| ELSE | Full | Handled as `ElseClause` inside `IfStmt`; no standalone execute needed |
| ELSEIF | Full | Handled as `ElseIfClause` inside `IfStmt`; evaluates condition and executes matching body |
| END | Full | Parse-time block terminator; no runtime execution needed |
| ENDDO | Full | Parse-time loop terminator; no runtime execution needed |
| ENDIF | Full | Parse-time block terminator; no runtime execution needed |
| ENDSL | Full | Parse-time SELECT terminator; no runtime execution needed |
| ENDSR | Full | Parse-time subroutine terminator; no runtime execution needed |
| EVAL | Partial | All assignment operators covered; `EVAL(M)` and `EVAL(R)` extenders hit `TODO` inside `DivExpr` |
| EVALR | Partial | Only `StringType` and `DataStructureType` targets allowed; throws for numeric or other result fields |
| EXCEPT | Not implemented | Immediately throws `NotImplementedError`; operation code not implemented |
| EXFMT | Full | Builds record from symbol table, calls `onExfmt` callback, and copies response back into data definitions |
| EXSR | Full | Full goto-within-subroutine and goto-to-top-level handling; `LeaveSrException` caught correctly |
| FEOD | Not implemented | `MockStatement`; execute is a no-op; forced end-of-data not actually performed |
| FOR | Full | Supports BY value, DOWNTO direction, ITER and LEAVE exceptions |
| GOTO | Partial | Throws `GotoException`; handled at subroutine and top-level loops; GOTO into nested structures may misbehave |
| IF | Full | Handles ELSEIF chains and ELSE clause; condition evaluated before each branch |
| IFEQ | Full | Old-form IF variant; handled as `IfStmt` with equality comparison expression |
| IFGE | Full | Old-form IF variant; handled as `IfStmt` with greater-or-equal comparison expression |
| IFGT | Full | Old-form IF variant; handled as `IfStmt` with greater-than comparison expression |
| IFLE | Full | Old-form IF variant; handled as `IfStmt` with less-or-equal comparison expression |
| IFLT | Full | Old-form IF variant; handled as `IfStmt` with less-than comparison expression |
| IFNE | Full | Old-form IF variant; handled as `IfStmt` with not-equal comparison expression |
| IN | Full | Calls `readDataArea` callback and assigns result; error indicator supported for lock failures |
| ITER | Full | Throws `IterException` caught by all loop execute methods |
| KFLD | Partial | Fields stored within `KListStmt`; actual key-type coercion delegated to `accessFieldsType` metadata |
| KLIST | Partial | Stores field list in `status.klists`; used by CHAIN/SETLL/SETGT via `toSearchValues()` |
| LEAVE | Full | Throws `LeaveException` caught by DO/DOW/DOU loop execute methods |
| LEAVESR | Full | Throws `LeaveSrException` caught by `ExecuteSubroutine` execute method |
| LOOKUP | Full | Calls `lookUp()` helper; sets HI/LO/EQ indicators; searches array sequentially or by position |
| MONITOR | Full | Executes body; catches `InterpreterProgramStatusErrorException`; routes to matching ON-ERROR clause |
| MOVE | Partial | Delegates to `move()` helper; type conversion may not cover all source/target type combinations |
| MOVEA | Full | Delegates to `movea()` helper; handles array-to-field and field-to-array moves |
| MOVEL | Partial | Delegates to `movel()` helper; left-aligned move; not all edge cases confirmed tested |
| MULT | Full | Supports optional Factor 1 and half-adjust extender; delegates to `mult()` helper |
| MVR | Partial | Execute is a no-op; remainder is stored to target by `DivStmt` during DIV execution |
| OCCUR | Full | Sets or gets occurrence index of an `OccurableDataStructValue`; error indicator for out-of-range |
| OPEN | Partial | Sets `dbFile.open = true` for DB files; printer files only log a warning and return |
| OREQ | Full | Handled via `continuedIndicators` mechanism on any statement |
| ORGE | Full | Handled via `continuedIndicators` mechanism on any statement |
| ORGT | Full | Handled via `continuedIndicators` mechanism on any statement |
| ORLE | Full | Handled via `continuedIndicators` mechanism on any statement |
| ORLT | Full | Handled via `continuedIndicators` mechanism on any statement |
| ORNE | Full | Handled via `continuedIndicators` mechanism on any statement |
| OTHER | Full | `SelectOtherClause` body executed directly by `SelectStmt`; `OtherStmt.execute()` has `TODO` but is never reached in normal SELECT context |
| OUT | Full | Calls `writeDataArea` callback; error indicator supported for write failures |
| PARM | Full | `PlistParam` carries parameter metadata; value copying handled by CALL and program execution |
| PLIST | Full | Verifies global symbol table contains each param; param type coercion done at call site |
| READ | Full | Delegates to `dbFile.read()`; LO and EQ indicators set from result |
| READE | Full | Delegates to `dbFile.readEqual()` or `dbFile.readEqual(kList)`; LO and EQ indicators set |
| READC | Not implemented | `MockStatement`; execute is a no-op; subfile READC not implemented |
| READP | Full | Delegates to `dbFile.readPrevious()`; LO and EQ indicators set from result |
| READPE | Full | Delegates to `dbFile.readPreviousEqual()` or with key list; LO and EQ indicators set |
| RESET | Partial | Restores `DataDefinition` to its `defaultValue`; `*ALL` operand and indicator targets not handled |
| RETURN | Partial | Sets RT indicator and throws `ReturnException`; LR indicator semantics not fully implemented |
| SCAN | Full | Finds all occurrences; sets EQ indicator and `lastFound`; supports array result field |
| SELECT | Full | Evaluates WHEN conditions in order; executes first matching body or OTHER clause |
| SETON | Full | Sets specified indicators to `true` via `SetStmt(ValueSet.ON)` |
| SETOFF | Full | Sets specified indicators to `false` via `SetStmt(ValueSet.OFF)` |
| SETGT | Full | Delegates to `dbFile.setgt(kList)`; positions file after the key |
| SETLL | Full | Delegates to `dbFile.setll(kList)`; positions file at or before the key |
| SORTA | Partial | Delegates to `sortA()` helper; keyed-DS-array sort may not be fully supported |
| SQL | Not implemented | `MockStatement`; sets `SQLCOD` to 100 as stub; actual SQL execution not implemented |
| SQRT | Full | No `SqrtStmt` class found; likely converted to EVAL with `%SQRT` during parse; `%SQRT` BIF is fully implemented |
| SUB | Full | Delegates to `sub()` helper; result field serves as minuend when Factor 1 is absent |
| SUBDUR | Partial | Delegates to `DiffExpr`; limited to cases where `asTimeStamp()` succeeds on both operands |
| SUBST | Full | Handles optional length and start; `(P)` extender replaces entire target string |
| TAG | Full | Execute is a no-op; serves as jump target resolved at execution time by GOTO/CAB |
| TESTN | Full | Distinguishes numeric, all-blank, leading-blank, and other-non-numeric string values via HI/LO/EQ |
| TIME | Partial | Handles `TimeStampType` and 6/12/14-digit `NumberType`; character result field not supported |
| UNLOCK | Not implemented | `MockStatement`; execute is a no-op; record unlocking not actually performed |
| UPDATE | Full | Builds `Record` from symbol table and delegates to `dbFile.update(record)` |
| WHEN | Full | `SelectCase` condition evaluated by `SelectStmt`; no standalone execute needed |
| WHENEQ | Full | `SelectCase` variant with equality operator; handled by `SelectStmt` |
| WHENGE | Full | `SelectCase` variant with greater-or-equal operator; handled by `SelectStmt` |
| WHENGT | Full | `SelectCase` variant with greater-than operator; handled by `SelectStmt` |
| WHENLE | Full | `SelectCase` variant with less-or-equal operator; handled by `SelectStmt` |
| WHENLT | Full | `SelectCase` variant with less-than operator; handled by `SelectStmt` |
| WHENNE | Full | `SelectCase` variant with not-equal operator; handled by `SelectStmt` |
| WRITE | Full | Builds `Record` from symbol table and delegates to `dbFile.write(record)` |
| XLATE | Full | Handles optional start position; builds full character-substitution map |
| XFOOT | Full | Sums array elements and assigns to result field; HI/LO/EQ indicators set |
| Z-ADD | Full | Delegates to `zadd()` helper; sign-forced assignment for numeric targets |
| Z-SUB | Full | Negates evaluated value and assigns to target field |
