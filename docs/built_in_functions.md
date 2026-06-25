# Built in functions

_Last refreshed: 2026-06-03_

In this document, we are going to list which **Built in functions** are so far implemented in Jariko.

| Built in function | Support level | Rationale | Example |
| ------ | ------ | ------ | ------ |
| %ABS | Full | Correctly computes absolute value via `abs()` for all numeric types | - |
| %ADDR | Not implemented | Implements `MockExpression`; always returns null pointer with no actual address resolution | - |
| %ALLOC | Not implemented | Implements `MockExpression`; always returns null pointer with no actual memory allocation | - |
| %CHAR | Full | Delegates to `stringRepresentation(format)` for both numeric and date formatting | - |
| %CHECK | Full | Handles start position and comparator-set scanning; returns first non-match position | - |
| %DEC | Partial | `DecTimeExpr` with custom format hits `TODO` in evaluator; numeric variant is fully implemented | `D d   S               D   INZ(D'2024-01-15')`<br>`C     EVAL      x = %DEC(d:*ISO)` — custom format on `DateValue` hits `TODO` |
| %DIFF | Partial | All duration codes dispatched, but every path calls `asTimeStamp()` which may fail for `DateValue` inputs | `D d1  S               D   INZ(D'2024-06-01')`<br>`C     EVAL      n = %DIFF(d1:d2:*DAYS)` — `DateValue` argument fails on `asTimeStamp()` |
| %EDITC | Partial | Class carries `// TODO add other parameters` comment; currency-symbol and asterisk-fill variants unconfirmed | `C     EVAL      r = %EDITC(val:'1':'$')` — 3rd currency-symbol parameter not confirmed |
| %EDITW | Partial | Class carries `// TODO add other parameters` comment; not all edit-word placeholder types confirmed covered | `C     EVAL      r = %EDITW(val:'   ,   .  CR')` — complex edit-word placeholder behavior unconfirmed |
| %ELEM | Partial | Handles `ArrayType` and `OccurableDataStructureType`; throws `IllegalStateException` for plain data structures | `D myDS          DS`<br>`C     EVAL      n = %ELEM(myDS)` — plain `DS` throws `IllegalStateException` |
| %EOF | Partial | Named-file variant reads `dbFileMap`; unnamed variant reads `lastDBFile` which may be stale across statements | `C     READ      FILE1`<br>`C     READ      FILE2`<br>`C     %EOF          IFEQ      *ON` — unnamed `%EOF` returns FILE2's stale state |
| %EQUAL | Partial | Named-file variant has `TODO` in `ExpressionEvaluation` | `C     %EQUAL(MYFILE)   IFEQ      *ON` — named-file variant hits `TODO` |
| %FOUND | Partial | Named-file variant has `TODO` in `ExpressionEvaluation` | `C     %FOUND(MYFILE)   IFEQ      *ON` — named-file variant hits `TODO` |
| %INT | Partial | `IntValue` input falls to `throw UnsupportedOperationException`; only `StringValue`, `DecimalValue`, and `UnlimitedStringValue` handled | `D x   S             10I 0 INZ(42)`<br>`C     EVAL      y = %INT(x)` — `IntValue` argument throws `UnsupportedOperationException` |
| %INTH | Full | Handles `StringValue`, `DecimalValue`, and `UnlimitedStringValue` with half-adjustment rounding | - |
| %LEN | Partial | Multiple `TODO` branches for `ArrayValue` and for `IntValue`/`DecimalValue` when `totalSize == 0` | `D arr S              5A   DIM(10)`<br>`C     EVAL      n = %LEN(arr(1) + 'X')` — non-`DataRefExpr` array expression hits `TODO` |
| %LOOKUP | Full | Linear search for unsorted arrays; binary search for ASCEND/DESCEND; start and length parameters supported | - |
| %LOOKUPGE | Full | Binary search for first value >= target; requires ASCEND or DESCEND array definition | - |
| %LOOKUPGT | Full | Binary search for first value > target; requires ASCEND or DESCEND array definition | - |
| %LOOKUPLE | Full | Binary search for last value <= target; requires ASCEND or DESCEND array definition | - |
| %LOOKUPLT | Full | Binary search for last value < target; requires ASCEND or DESCEND array definition | - |
| %OPEN | Full | Reads `open` flag from `EnrichedDBFile`; requires non-null file-name argument | - |
| %PARMS | Partial | Returns caller-parameter count; does not validate that it is called only inside a procedure or program | `D n   S             10I 0`<br>`C     EVAL      n = %PARMS` — returns a value at module level without raising the required error |
| %REALLOC | Not implemented | Implements `MockExpression`; delegates to `onMockExpression` callback with no actual memory reallocation | - |
| %REM | Full | Correctly computes integer remainder via `n % m` | - |
| %REPLACE | Full | All three overload forms (no-start, start-only, start+length) correctly implemented | - |
| %SCAN | Full | Optional start and length parameters supported; returns 0 when not found | - |
| %SIZE | Partial | Only `DataRefExpr` handled; literals, arrays with `*ALL`, and tables throw `UnsupportedOperationException` | `C     EVAL      n = %SIZE('ABC')` — literal argument throws `UnsupportedOperationException` |
| %SQRT | Full | Raises `ProgramStatusCode.NEGATIVE_SQUARE_ROOT` for negative input per RPG spec | - |
| %SUBST | Full | Works as r-value and l-value (assignable); padding and length semantics match RPG spec | - |
| %SUBARR | Full | Handles optional element-count argument; works as r-value and l-value (assignable) | - |
| %TIMESTAMP | Partial | Non-null non-string value hits `TODO` in `ExpressionEvaluation` | `D n   S             14P 0 INZ(20240101120000)`<br>`C     EVAL      ts = %TIMESTAMP(n)` — non-string value hits `TODO` |
| %TRIM | Partial | Optional `charactersToTrim` parameter is silently ignored in eval; only whitespace is trimmed | `C     EVAL      r = %TRIM(str:'*')` — `'*'` argument silently ignored; only whitespace trimmed |
| %TRIML | Full | Optional `charactersToTrim` correctly strips prefix characters | - |
| %TRIMR | Full | Optional `charactersToTrim` correctly strips suffix characters | - |
| %XLATE | Full | Handles optional start position; builds full character-substitution map | - |
| %XFOOT | Full | Sums all numeric array elements; enforces array-only argument | - |