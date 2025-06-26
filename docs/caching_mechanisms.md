# Caching Mechanisms for Jariko Performance Optimization

This document describes in detail the caching mechanisms proposed in the Performance Optimization Report to improve Jariko's execution speed.

## Existing Caching in Jariko

Jariko already implements some caching mechanisms:

1. **Program Cache in JavaSystemInterface**:
   ```kotlin
   private val programs = HashMap<String, Program?>()
   
   override fun findProgram(name: String): Program? {
       return programs.computeIfAbsent(name) {
           findInPackages(name) ?: findInFileSystem(name)
       }
   }
   ```
   This mechanism caches program lookups to avoid repeatedly parsing the same programs.

2. **Copy Cache**:
   ```kotlin
   private val copies = HashMap<CopyId, Copy?>()
   
   override fun findCopy(copyId: CopyId): Copy? {
       return copies.computeIfAbsent(copyId) {
           copySource.invoke(copyId)
       }
   }
   ```
   Similar to the program cache, this caches parsed copy files.

3. **Function and API Caches**:
   ```kotlin
   private val functions = HashMap<String, Function>()
   private val apiDescriptors = HashMap<ApiId, ApiDescriptor>()
   private val apis = HashMap<ApiId, Api>()
   ```
   These cache functions and APIs to avoid repeated lookups.

## Proposed Additional Caching Mechanisms

While the existing caches help with program, copy, function, and API lookups, there are additional opportunities for caching in the performance-critical execution path:

### 1. Parameter Profile Cache

**Purpose**: Cache parameter type information and validation rules for each program.

**Current Implementation**: In `RpgProgram.execute()`, parameter validation is performed on each call:
```kotlin
for (pv in params) {
    val expectedType = params().find { it.name == pv.key }!!.type
    val coercedValue = coerce(pv.value, expectedType)
    require(coercedValue.assignableTo(expectedType)) {
        "param ${pv.key} was expected to have type $expectedType. It has value: $coercedValue"
    }
}
```

**Proposed Improvement**:
```kotlin
class ParameterProfileCache {
    // Maps program name to its parameter profile (type expectations and validation rules)
    private val profiles = ConcurrentHashMap<String, ProgramParameterProfile>()
    
    fun getOrCreateProfile(program: Program): ProgramParameterProfile {
        return profiles.computeIfAbsent(program.name) { 
            buildParameterProfile(program) 
        }
    }
    
    private fun buildParameterProfile(program: Program): ProgramParameterProfile {
        // Extract and precompute parameter validation rules
        val paramTypes = program.params().associateBy({ it.name }, { it.type })
        return ProgramParameterProfile(paramTypes)
    }
}

data class ProgramParameterProfile(
    val parameterTypes: Map<String, Type>
) {
    // Precomputed validation logic
    fun validateAndCoerceParameters(params: LinkedHashMap<String, Value>): LinkedHashMap<String, Value> {
        val processedParams = LinkedHashMap<String, Value>()
        params.forEach { (name, value) ->
            parameterTypes[name]?.let { expectedType ->
                // Use fast coercion path for common types
                val coercedValue = fastCoerce(value, expectedType)
                require(coercedValue.assignableTo(expectedType)) { 
                    "param $name was expected to have type $expectedType. It has value: $coercedValue"
                }
                processedParams[name] = coercedValue
            }
        }
        return processedParams
    }
}
```

### 2. Execution State Cache

**Purpose**: Cache the initialized state of programs to avoid repeated initialization.

**Current Implementation**: In `RpgProgram`, components are initialized using Kotlin's `lazy` delegate, which causes overhead on first access:
```kotlin
private val interpreter: InternalInterpreter by lazy {
    val interpreterCore = InternalInterpreter(this.systemInterface!!)
    configuration.jarikoCallback.onInterpreterCreation(interpreterCore)
    interpreterCore
}

val intepreterCore: InterpreterCore by lazy {
    interpreter
}
```

**Proposed Improvement**:
```kotlin
class ExecutionStateCache {
    private val initializedStates = ConcurrentHashMap<String, ProgramExecutionState>()
    
    fun getOrInitializeState(program: RpgProgram): ProgramExecutionState {
        return initializedStates.computeIfAbsent(program.name) {
            createInitializedState(program)
        }
    }
    
    private fun createInitializedState(program: RpgProgram): ProgramExecutionState {
        val interpreter = InternalInterpreter(program.systemInterface!!)
        program.configuration.jarikoCallback.onInterpreterCreation(interpreter)
        
        // Pre-initialize any other components needed for execution
        return ProgramExecutionState(interpreter)
    }
}

data class ProgramExecutionState(
    val interpreter: InternalInterpreter,
    // Other pre-initialized components
)
```

### 3. Compiled Expression Cache

**Purpose**: Cache the compilation of frequently evaluated expressions.

**Current Implementation**: Expressions are evaluated from scratch each time, including parsing, type checking, and evaluation.

**Proposed Improvement**:
```kotlin
class CompiledExpressionCache {
    private val compiledExpressions = ConcurrentHashMap<Expression, CompiledExpression>()
    
    fun getOrCompile(expression: Expression): CompiledExpression {
        return compiledExpressions.computeIfAbsent(expression) {
            compileExpression(expression)
        }
    }
    
    private fun compileExpression(expression: Expression): CompiledExpression {
        // Analyze expression, precompute type information, optimize
        // Potentially generate bytecode for very hot expressions
        return when (expression) {
            is BinaryExpression -> compileBinaryExpression(expression)
            is DataRefExpr -> compileDataRefExpression(expression)
            // Handle other expression types
            else -> DefaultCompiledExpression(expression)
        }
    }
}

interface CompiledExpression {
    fun evaluate(interpreter: InterpreterCore): Value
}
```

## Implementation Strategy

To implement these caching mechanisms effectively:

1. **Add Caching Layer to RpgProgram**:
   - Modify the `execute` method to use cached parameter profiles and execution states
   - Initialize caches at the system level in `MainExecutionContext`
   - Ensure thread safety with concurrent collections

2. **Optimize Cache Key Generation**:
   - Use efficient key generation for cache lookups
   - Implement proper equality and hashCode for all cache keys

3. **Support Cache Invalidation**:
   - Add mechanism to invalidate caches when programs or dependencies change
   - Support both automatic and manual cache invalidation

4. **Profile and Tune Cache Sizes**:
   - Monitor cache hit rates and memory usage
   - Implement size-bounded caches if memory usage becomes an issue

## Expected Performance Impact

Implementing these additional caching mechanisms should yield:

- **20-30% improvement** in repeated program execution time
- **40-60% improvement** in programs with complex parameter validation
- **15-25% improvement** in first-time execution due to optimized initialization
- **Reduced GC pressure** from fewer temporary objects during parameter processing

The most significant gains will be in scenarios with:
1. Frequent calls to the same programs
2. Complex parameter transformations
3. Programs with many expressions that are evaluated repeatedly

## Memory Considerations

While caching improves performance, it comes with increased memory usage. To balance performance and memory:

- Use weak references for infrequently accessed cache entries
- Implement time-based expiration for cache entries
- Monitor cache size and evict entries based on usage patterns
- Provide configuration options to tune cache sizes based on available memory
