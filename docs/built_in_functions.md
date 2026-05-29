# Built in functions

_Last refreshed: 2026-05-29_

In this document, we are going to list which **Built in functions** are so far implemented in Jariko.

| Built in function | Support level | Rationale |
| ------ | ------ | ------ |
| %ABS | Full | Correctly computes absolute value via `abs()` for all numeric types |
| %ADDR | Not implemented | Implements `MockExpression`; always returns null pointer with no actual address resolution |
| %ALLOC | Not implemented | Implements `MockExpression`; always returns null pointer with no actual memory allocation |
| %CHAR | Full | Delegates to `stringRepresentation(format)` for both numeric and date formatting |
| %CHECK | Full | Handles start position and comparator-set scanning; returns first non-match position |
| %DEC | Partial | `DecTimeExpr` with custom format hits `TODO` in evaluator; numeric variant is fully implemented |
| %DIFF | Partial | All duration codes dispatched, but every path calls `asTimeStamp()` which may fail for `DateValue` inputs |
| %EDITC | Partial | Class carries `// TODO add other parameters` comment; currency-symbol and asterisk-fill variants unconfirmed |
| %EDITW | Partial | Class carries `// TODO add other parameters` comment; not all edit-word placeholder types confirmed covered |
| %ELEM | Partial | Handles `ArrayType` and `OccurableDataStructureType`; throws `IllegalStateException` for plain data structures |
| %EOF | Partial | Named-file variant reads `dbFileMap`; unnamed variant reads `lastDBFile` which may be stale across statements |
| %EQUAL | Partial | Named-file variant has `TODO` in `ExpressionEvaluation` |
| %FOUND | Partial | Named-file variant has `TODO` in `ExpressionEvaluation` |
| %INT | Partial | `IntValue` input falls to `throw UnsupportedOperationException`; only `StringValue`, `DecimalValue`, and `UnlimitedStringValue` handled |
| %INTH | Full | Handles `StringValue`, `DecimalValue`, and `UnlimitedStringValue` with half-adjustment rounding |
| %LEN | Partial | Multiple `TODO` branches for `ArrayValue` and for `IntValue`/`DecimalValue` when `totalSize == 0` |
| %LOOKUP | Full | Linear search for unsorted arrays; binary search for ASCEND/DESCEND; start and length parameters supported |
| %LOOKUPGE | Full | Binary search for first value >= target; requires ASCEND or DESCEND array definition |
| %LOOKUPGT | Full | Binary search for first value > target; requires ASCEND or DESCEND array definition |
| %LOOKUPLE | Full | Binary search for last value <= target; requires ASCEND or DESCEND array definition |
| %LOOKUPLT | Full | Binary search for last value < target; requires ASCEND or DESCEND array definition |
| %OPEN | Full | Reads `open` flag from `EnrichedDBFile`; requires non-null file-name argument |
| %PARMS | Partial | Returns caller-parameter count; does not validate that it is called only inside a procedure or program |
| %REALLOC | Not implemented | Implements `MockExpression`; delegates to `onMockExpression` callback with no actual memory reallocation |
| %REM | Full | Correctly computes integer remainder via `n % m` |
| %REPLACE | Full | All three overload forms (no-start, start-only, start+length) correctly implemented |
| %SCAN | Full | Optional start and length parameters supported; returns 0 when not found |
| %SIZE | Partial | Only `DataRefExpr` handled; literals, arrays with `*ALL`, and tables throw `UnsupportedOperationException` |
| %SQRT | Full | Raises `ProgramStatusCode.NEGATIVE_SQUARE_ROOT` for negative input per RPG spec |
| %SUBST | Full | Works as r-value and l-value (assignable); padding and length semantics match RPG spec |
| %SUBARR | Full | Handles optional element-count argument; works as r-value and l-value (assignable) |
| %TIMESTAMP | Partial | Non-null non-string value hits `TODO` in `ExpressionEvaluation` |
| %TRIM | Partial | Optional `charactersToTrim` parameter is silently ignored in eval; only whitespace is trimmed |
| %TRIML | Full | Optional `charactersToTrim` correctly strips prefix characters |
| %TRIMR | Full | Optional `charactersToTrim` correctly strips suffix characters |
| %XLATE | Full | Handles optional start position; builds full character-substitution map |
| %XFOOT | Full | Sums all numeric array elements; enforces array-only argument |
