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
PROF* SPANSTART <span_id> ["optional comment"]
    // RPG code to trace
PROF* SPANEND
```

#### Components:

- **`PROF*`**: The profiling annotation prefix (case-insensitive)
- **`SPANSTART`**: Marks the beginning of a trace span (case-insensitive)
- **`SPANEND`**: Marks the end of a trace span (case-insensitive)
- **`<span_id>`**: A unique identifier for the span (can contain letters, numbers, and special characters: `§£#@$_`)
- **`"optional comment"`**: An optional descriptive comment enclosed in double quotes

### Basic Example

```rpgle
V* ==============================================================
V* Basic RPG Profiling Example
V* ==============================================================
D A               S              8  0 INZ(5)
D B               S              8  0 INZ(8)
D RESULT          S              8  0 INZ(0)
 *

PROF* SPANSTART CALCULATION "Simple addition operation"
C                   EVAL      RESULT = A + B
PROF* SPANEND

C                   SETON                                        LR
```

### Advanced Examples

#### Multiple Nested Spans

```rpgle
V* ==============================================================
V* Nested Profiling Spans Example
V* ==============================================================
D COUNTER         S              3  0
D RESULT          S              8  0 INZ(0)
D A               S              8  0 INZ(5)
D B               S              8  0 INZ(8)
 *

PROF* SPANSTART MAIN_LOOP "Main processing loop"
C                   FOR       COUNTER = 1 TO 10

PROF* SPANSTART CALCULATION "Inner calculation"
C                   EVAL      RESULT = A + B * COUNTER
PROF* SPANEND

PROF* SPANSTART DISPLAY "Display result"
C     RESULT        DSPLY
PROF* SPANEND

C                   ENDFOR
PROF* SPANEND

C                   SETON                                        LR
```

#### Conditional Processing with Profiling

```rpgle
V* ==============================================================
V* Conditional Processing with Profiling
V* ==============================================================
D STATUS          S              1A INZ('A')
D AMOUNT          S             10  2 INZ(100.50)
D RESULT          S             10  2
 *

PROF* SPANSTART VALIDATION "Input validation"
C                   SELECT

C                   WHEN      STATUS = 'A'
PROF* SPANSTART ACTIVE_PROCESSING "Process active records"
C                   EVAL      RESULT = AMOUNT * 1.1
PROF* SPANEND

C                   WHEN      STATUS = 'I'
PROF* SPANSTART INACTIVE_PROCESSING "Process inactive records"
C                   EVAL      RESULT = AMOUNT * 0.9
PROF* SPANEND

C                   OTHER
PROF* SPANSTART ERROR_PROCESSING "Handle error cases"
C                   EVAL      RESULT = 0
PROF* SPANEND

C                   ENDSL
PROF* SPANEND

C                   SETON                                        LR
```

#### Database Operations Profiling

```rpgle
V* ==============================================================
V* Database Operations Profiling Example
V* ==============================================================
D CUSTOMER_ID     S             10A
D CUSTOMER_NAME   S             30A
D ORDER_COUNT     S              5  0
 *

PROF* SPANSTART DB_SETUP "Database connection setup"
// Database setup code here
PROF* SPANEND

PROF* SPANSTART CUSTOMER_READ "Read customer data"
// Read customer record
C                   EVAL      CUSTOMER_ID = 'CUST001'
C                   EVAL      CUSTOMER_NAME = 'John Doe'
PROF* SPANEND

PROF* SPANSTART ORDER_COUNT "Count customer orders"
// Count orders for customer
C                   EVAL      ORDER_COUNT = 5
PROF* SPANEND

PROF* SPANSTART REPORT_GEN "Generate customer report"
C                   EVAL      %SUBST(CUSTOMER_NAME:1:10) = 'Report for'
PROF* SPANEND

C                   SETON                                        LR
```

#### Performance Testing with Timing

```rpgle
V* ==============================================================
V* Performance Testing Example
V* ==============================================================
D LOOP_COUNT      S              7  0
D COUNTER         S              7  0
D RESULT          S             15  5
D START_TIME      S               Z
D END_TIME        S               Z
D ELAPSED_MS      S             10  0
 *

C                   EVAL      LOOP_COUNT = 100000

PROF* SPANSTART PERFORMANCE_TEST "Large loop performance test"

C                   TIME                    START_TIME

PROF* SPANSTART CALCULATION_LOOP "Main calculation loop"
C     1             DO        LOOP_COUNT    COUNTER
C                   EVAL      RESULT = %SQRT(COUNTER * 3.14159)
C                   ENDDO
PROF* SPANEND

C                   TIME                    END_TIME
C     END_TIME      SUBDUR    START_TIME    ELAPSED_MS:*MS

PROF* SPANSTART RESULT_DISPLAY "Display performance results"
C                   EVAL      ELAPSED_MS = ELAPSED_MS / 1000
C     'Performance test completed'  DSPLY
C     ELAPSED_MS    DSPLY
PROF* SPANEND

PROF* SPANEND

C                   SETON                                        LR
```

## Best Practices

### 1. Meaningful Span Names
Use descriptive names that clearly indicate what the span is measuring:
```rpgle
PROF* SPANSTART CUSTOMER_VALIDATION "Validate customer data integrity"
PROF* SPANSTART DB_CUSTOMER_READ "Read customer from database"
PROF* SPANSTART CALC_DISCOUNT "Calculate customer discount percentage"
```

### 2. Proper Nesting
Ensure spans are properly nested and closed:
```rpgle
PROF* SPANSTART OUTER "Outer operation"
PROF* SPANSTART INNER "Inner operation"
// code here
PROF* SPANEND
PROF* SPANEND
```

### 3. Granular Measurement
Use spans to isolate specific operations for performance analysis:
```rpgle
PROF* SPANSTART FILE_IO "File input/output operations"
// File operations
PROF* SPANEND

PROF* SPANSTART CALCULATIONS "Business logic calculations"
// Calculation code
PROF* SPANEND
```

### 4. Include Comments
Add meaningful comments to provide context:
```rpgle
PROF* SPANSTART BATCH_PROCESS "Process 10,000 customer records in batch"
PROF* SPANSTART VALIDATION "Validate customer data against business rules"
```

## Integration with Copy Members

Profiling annotations can be used in copy members that are included in your programs:

**COPY_WITH_PROFILING.rpgle**:
```rpgle
PROF* SPANSTART COPY_OPERATION "Shared business logic from copy member"
C                   EVAL      RESULT = INPUT_VALUE * 2
PROF* SPANEND
```

**Main Program**:
```rpgle
D INPUT_VALUE     S             10  2 INZ(50.25)
D RESULT          S             10  2
 *
PROF* SPANSTART MAIN_PROCESSING "Main program processing"
/COPY COPY_WITH_PROFILING
PROF* SPANEND

C                   SETON                                        LR
```

## Troubleshooting

### Common Issues

1. **Spans Not Appearing**: Ensure `profilingSupport = true` in your configuration
2. **Unmatched Spans**: Check that every `SPANSTART` has a corresponding `SPANEND`
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
