# RPG Profiling and Tracing

Jariko provides a powerful profiling and tracing system that allows you to monitor RPG program execution at a granular level. This system enables you to:

- Measure execution time of specific code blocks
- Track program flow and statement execution
- Create custom telemetry spans with profiling annotations
- Capture detailed performance metrics

## Overview

The profiling system allows you to define custom trace spans directly in your RPG source code using profiling annotations.

## Enabling RPG Trace Support

To enable RPG trace support, you need to configure the profiling system in your Jariko configuration:

### Configuration Options

In your `Configuration` object, set the `profilingSupport` option to `true`:

```kotlin
val configuration = Configuration().apply {
    options = Options().apply {
        profilingSupport = true
    }
    jarikoCallback.startRpgTrace = { trace ->
        // Handle trace start
        println("Starting trace: ${trace.fullName}")
    }
    jarikoCallback.finishRpgTrace = {
        // Handle trace end
        println("Finished trace")
    }
}
```

## RPG Profiling Annotations

Profiling annotations allow you to define custom trace spans directly in your RPG source code. These annotations use a special comment syntax that Jariko recognizes and processes during execution.

### Annotation Syntax

Profiling annotations use the following syntax:

```rpgle
 COP* *TRACE
    * @StartTrace M(<span_id>) ["optional comment"]
    // RPG code to trace
    *@StopTrace [M(<span_id>)] [T(XXX &A(VAR1);&A(VAR2))] ["optional comment"]
```

#### Components:

- **`COP* *TRACE`**: The profiling annotation that enable annotation in the program (case-insensitive)
- **`@StopTrace`**: Marks the beginning of a trace span (case-insensitive) identified by `<span_id>`
- **`@StopTrace`**: Marks the end of a trace span (case-insensitive) optionally identified by `<span_id>`
- **`M(<span_id>)`**: A unique identifier for the span (can contain letters, numbers, and special characters: `§£#@$_`)
- **`T(XXX &A(<var1>);&A(<var2>))`**: An optional list of variables to keep trace of, separated by ';'
- **`"optional comment"`**: An optional descriptive comment enclosed in double quotes
### Basic Example

```rpgle
     V* ==============================================================
     V* Basic RPG Profiling Example
     V* ==============================================================
   COP* *TRACE
     D A               S              8  0 INZ(5)
     D B               S              8  0 INZ(8)
     D RESULT          S              8  0 INZ(0)
      *
      *@StartTrace M(CALCULATION) "Simple addition operation"
     C                   EVAL      RESULT = A + B
      *@StopTrace M(CALCULATION)
     C                   SETON                                        LR
```

#### Basic Example with capture

```rpgle
     V* ==============================================================
     V* Basic RPG Profiling Example with capture
     V* ==============================================================
   COP* *TRACE
     D A               S              8  0 INZ(5)
     D B               S              8  0 INZ(8)
     D RESULT          S              8  0 INZ(0)
      *
      *@StartTrace M(CALCULATION) "Simple addition operation"
     C                   EVAL      RESULT = A + B
      *@StopTrace M(CALCULATION) T(XXX &A(RESULT);&A(A);&A(B))
     C                   SETON                                        LR
```

### Advanced Examples

#### Multiple Nested Spans

```rpgle
     V* ==============================================================
     V* Nested Profiling Spans Example
     V* ==============================================================
   COP* *TRACE
     D COUNTER         S              3  0
     D RESULT          S              8  0 INZ(0)
     D A               S              8  0 INZ(5)
     D B               S              8  0 INZ(8)
      *
      *@StartTrace M(MAIN_LOOP) "Main processing loop"
     C                   FOR       COUNTER = 1 TO 10
      *@StartTrace M(CALCULATION) "Inner calculation"
     C                   EVAL      RESULT = A + B * COUNTER
      * @StopTrace M(CALCULATION)
      *@StartTrace M(DISPLAY) "Display result"
     C     RESULT        DSPLY
      * @StopTrace M(DISPLAY)
     C                   ENDFOR
      * @StopTrace M(MAIN_LOOP)
     C                   SETON                                        LR
```

#### Conditional Processing with Profiling

```rpgle
     V* ==============================================================
     V* Conditional Processing with Profiling
     V* ==============================================================
   COP* *TRACE
     D STATUS          S              1A INZ('A')
     D AMOUNT          S             10  2 INZ(100.50)
     D RESULT          S             10  2
      *
      *@StartTrace M(VALIDATION) "Input validation"
     C                   SELECT
     C                   WHEN      STATUS = 'A'
      *@StartTrace M(ACTIVE_PROCESSING) "Process active records"
     C                   EVAL      RESULT = AMOUNT * 1.1
      * @StopTrace M(ACTIVE_PROCESSING)
     C                   WHEN      STATUS = 'I'
      *@StartTrace M(INACTIVE_PROCESSING) "Process inactive records"
     C                   EVAL      RESULT = AMOUNT * 0.9
      * @StopTrace M(INACTIVE_PROCESSING)
     C                   OTHER
      *@StartTrace M(ERROR_PROCESSING) "Handle error cases"
     C                   EVAL      RESULT = 0
      * @StopTrace M(ERROR_PROCESSING)
     C                   ENDSL
      * @StopTrace M(VALIDATION)
     C                   SETON                                        LR
```

#### Database Operations Profiling

```rpgle
     V* ==============================================================
     V* Database Operations Profiling Example
     V* ==============================================================
   COP* *TRACE
     D CUSTOMER_ID     S             10A
     D CUSTOMER_NAME   S             30A
     D ORDER_COUNT     S              5  0
      *
      *@StartTrace M(DB_SETUP) "Database connection setup"
      // Database setup code here
      * @StopTrace M(DB_SETUP)
      *@StartTrace M(CUSTOMER_READ) "Read customer data"
      // Read customer record
     C                   EVAL      CUSTOMER_ID = 'CUST001'
     C                   EVAL      CUSTOMER_NAME = 'John Doe'
      * @StopTrace M(CUSTOMER_READ)
      *@StartTrace M(ORDER_COUNT) "Count customer orders"
      // Count orders for customer
     C                   EVAL      ORDER_COUNT = 5
      * @StopTrace M(ORDER_COUNT)
      *@StartTrace M(REPORT_GEN) "Generate customer report"
     C                   EVAL      %SUBST(CUSTOMER_NAME:1:10) = 'Report for'
      * @StopTrace M(REPORT_GEN)
     C                   SETON                                        LR
```

#### Performance Testing with Timing

```rpgle
     V* ==============================================================
     V* Performance Testing Example
     V* ==============================================================
   COP* *TRACE
     D LOOP_COUNT      S              7  0
     D COUNTER         S              7  0
     D RESULT          S             15  5
     D START_TIME      S               Z
     D END_TIME        S               Z
     D ELAPSED_MS      S             10  0
      *
     C                   EVAL      LOOP_COUNT = 100000
      *@StartTrace M(PERFORMANCE_TEST) "Large loop performance test"
     C                   TIME                    START_TIME
      *@StartTrace M(CALCULATION_LOOP) "Main calculation loop"
     C     1             DO        LOOP_COUNT    COUNTER
     C                   EVAL      RESULT = %SQRT(COUNTER * 3.14159)
     C                   ENDDO
      * @StopTrace M(CALCULATION_LOOP)
     C                   TIME                    END_TIME
     C     END_TIME      SUBDUR    START_TIME    ELAPSED_MS:*MS
      *@StartTrace M(RESULT_DISPLAY) "Display performance results"
     C                   EVAL      ELAPSED_MS = ELAPSED_MS / 1000
     C     'Performance test completed'  DSPLY
     C     ELAPSED_MS    DSPLY
      * @StopTrace M(RESULT_DISPLAY)
      * @StopTrace M(PERFORMANCE_TEST)
     C                   SETON                                        LR
```

## Best Practices

### 1. Meaningful Span Names
Use descriptive names that clearly indicate what the span is measuring:
```rpgle
      *@StartTrace M(CUSTOMER_VALIDATION) "Validate customer data integrity"
      *@StartTrace M(DB_CUSTOMER_READ) "Read customer from database"
      *@StartTrace M(CALC_DISCOUNT) "Calculate customer discount percentage"
```

### 2. Proper Nesting
Ensure spans are properly nested and closed:
```rpgle
      *@StartTrace M(OUTER) "Outer operation"
      *@StartTrace M(INNER) "Inner operation"
      // code here
      * @StopTrace M(INNER)
      * @StopTrace M(OUTER)
```

### 3. Granular Measurement
Use spans to isolate specific operations for performance analysis:
```rpgle
      *@StartTrace M(FILE_IO) "File input/output operations"
      // File operations
      * @StopTrace M(FILE_IO)
      *@StartTrace M(CALCULATIONS) "Business logic calculations"
      // Calculation code
      * @StopTrace M(CALCULATIONS)
```

### 4. Include Comments
Add meaningful comments to provide context:
```rpgle
      *@StartTrace M(BATCH_PROCESS) "Process 10,000 customer records in batch"
      *@StartTrace M(VALIDATION) "Validate customer data against business rules"
```

### 5. Include profile annotation
Include `COP* *TRACE` profile annotation before variable declaration for consistency:
```rpgle
     V* ==============================================================
     V* Basic RPG Profiling Example
     V* ==============================================================
   COP* *TRACE
     D A               S              8  0 INZ(5)
     D B               S              8  0 INZ(8)
     D RESULT          S              8  0 INZ(0)
      *
      *@StartTrace M(CALCULATION) "Simple addition operation"
     C                   EVAL      RESULT = A + B
      *@StopTrace M(CALCULATION) T(XXX &A(RESULT);&A(A);&A(B))
     C                   SETON                                        LR
```

## Integration with Copy Members

Profiling annotations can be used in copy members that are included in your programs:

**COPY_WITH_PROFILING.rpgle**:
```rpgle
      *@StartTrace M(COPY_OPERATION) "Shared business logic from copy member"
     C                   EVAL      RESULT = INPUT_VALUE * 2
      * @StopTrace M(COPY_OPERATION)
```

**Main Program**:
```rpgle
   COP* *TRACE
     D INPUT_VALUE     S             10  2 INZ(50.25)
     D RESULT          S             10  2
      *
      *@StartTrace M(MAIN_PROCESSING) "Main program processing"
      /COPY COPY_WITH_PROFILING
      * @StopTrace M(MAIN_PROCESSING)
     C                   SETON                                        LR
```

## Troubleshooting

### Common Issues

1. **Spans Not Appearing**: Ensure `profilingSupport = true` in your configuration and `COP* *TRACE` is included
2. **Unmatched Spans**: Check that every `@StartTrace` has a corresponding `@StopTrace`
3. **Invalid Span IDs**: Span IDs can only contain letters, numbers, and these special characters: `§£#@$_`

### Debugging Profiling

Enable verbose logging to see profiling annotation processing:
```kotlin
val configuration = Configuration().apply {
    options = Options().apply {
        profilingSupport = true
        debuggingInformation = true
    }
}
```

## Performance Considerations

- Profiling adds minimal overhead to execution
- Use profiling annotations judiciously in production code
- Consider using conditional compilation for profiling code in production environments
- The profiling system is designed to be lightweight and non-intrusive

## See Also

- [Logging Documentation](logging.md) - For general logging configuration
- [Development Guide](development.md) - For development environment setup
- [Performance Tests](performance_tests.md) - For performance testing guidelines
