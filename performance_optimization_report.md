# Jariko Performance Optimization Report

## Overview

This report analyzes performance bottlenecks in the RPG interpreter's execution flow and proposes concrete optimization strategies for the `execute` method in `RpgProgram` class, which serves as the entry point for the application.

## Current Implementation Analysis

The `execute` method in the `RpgProgram` class shows several potential performance bottlenecks:

1. **Lazy Initialization Pattern**: Multiple components are initialized using Kotlin's `lazy` delegate, which can cause delays during first execution
2. **Extensive Parameter Validation**: The method performs extensive parameter checking before actual execution
3. **Logging Overhead**: Comprehensive logging is performed throughout execution
4. **Symbol Table Operations**: Frequent access to the global symbol table
5. **Value Coercion**: Type coercion for parameters happens individually for each parameter
6. **Multiple Data Transformations**: Data is transformed between different formats during execution

## Optimization Strategies

### 1. Implement Caching Mechanisms

**Issue**: The interpreter repeatedly accesses and validates parameters and program definitions.

**Solution**:
- Implement a cache for frequently accessed program definitions and parameter profiles
- Pre-compute parameter validation requirements and store them in a lookup structure
- Cache compiled versions of frequently executed programs

```kotlin
// Example implementation of a program cache
class ProgramCache {
    private val compiledPrograms = ConcurrentHashMap<String, CompiledProgram>()
    
    fun getOrCompile(name: String, compileFunction: () -> CompiledProgram): CompiledProgram {
        return compiledPrograms.computeIfAbsent(name) { compileFunction() }
    }
}
```

**Expected Impact**: 20-30% performance improvement for repeated program executions.

### 2. Optimize Parameter Processing

**Issue**: Parameter validation and type coercion is performed sequentially for each parameter.

**Solution**: 
- Batch parameter processing where possible
- Pre-compute expected parameter types and validation rules
- Optimize the coercion function for common use cases

```kotlin
// Optimized batch parameter processing
private fun batchProcessParameters(params: LinkedHashMap<String, Value>, expectedTypes: Map<String, Type>): LinkedHashMap<String, Value> {
    val processedParams = LinkedHashMap<String, Value>()
    params.forEach { (name, value) ->
        expectedTypes[name]?.let { type ->
            // Use specialized coercion paths for common types
            processedParams[name] = fastCoerce(value, type)
        }
    }
    return processedParams
}
```

**Expected Impact**: 10-15% improvement in parameter processing time.

### 3. Reduce Logging Overhead

**Issue**: Extensive logging throughout execution creates significant overhead.

**Solution**:
- Implement conditional logging that can be completely disabled in production
- Use logging levels more effectively to reduce overhead
- Buffer log messages and write them in batches
- Implement async logging to avoid blocking the execution thread

```kotlin
// Example of optimized conditional logging
private inline fun conditionalLog(level: LogLevel, crossinline messageProducer: () -> LazyLogEntry) {
    if (MainExecutionContext.isLoggingEnabled && level.shouldLog()) {
        logHandlers.renderLog { messageProducer() }
    }
}
```

**Expected Impact**: Up to 25% improvement in scenarios with extensive logging.

### 4. Optimize Symbol Table Operations

**Issue**: Symbol table operations are frequent and potentially costly.

**Solution**: 
- Implement a more efficient data structure for the symbol table
- Use specialized data structures for specific types of lookups
- Implement read/write optimized versions of the symbol table
- Consider using a two-tier symbol table (frequently used vs. rarely used symbols)

```kotlin
// Example of an optimized symbol table implementation
class OptimizedSymbolTable : ISymbolTable {
    // Fast access map for frequently used symbols
    private val hotSymbols = HashMap<String, Value>()
    // Regular map for other symbols
    private val regularSymbols = HashMap<String, Value>()
    
    override operator fun get(name: String): Value {
        return hotSymbols[name] ?: regularSymbols[name] ?: throw SymbolNotFoundException(name)
    }
    
    // Track access frequency and promote/demote symbols accordingly
}
```

**Expected Impact**: 15-20% improvement in symbol table operations.

### 5. Implement Just-In-Time Compilation

**Issue**: Interpreted execution is inherently slower than compiled code.

**Solution**:
- Implement a simple JIT compiler for frequently executed code paths
- Convert hot spots in RPG code to optimized JVM bytecode during runtime
- Implement profile-guided optimization to identify hot spots

### Detailed Implementation Strategy

#### 5.1 Hot Spot Detection

Before implementing JIT compilation, we need to identify which code paths are executed frequently enough to justify compilation:

```kotlin
class HotSpotTracker {
    private val executionCounts = ConcurrentHashMap<String, AtomicInteger>()
    private val compilationThreshold = 1000 // Configurable threshold
    
    fun recordExecution(statementId: String) {
        val count = executionCounts.computeIfAbsent(statementId) { AtomicInteger(0) }
        if (count.incrementAndGet() >= compilationThreshold && !JITCompiler.isCompiled(statementId)) {
            JITCompiler.scheduleCompilation(statementId)
        }
    }
}
```

This tracker would be integrated into the interpreter's statement execution flow to count executions of each code block.

#### 5.2 JIT Compilation Architecture

JIT compilation for Jariko should follow these steps:

1. **IR Generation**: Convert RPG AST to an intermediate representation (IR)
2. **Optimization**: Apply optimizations on the IR
3. **Bytecode Generation**: Generate JVM bytecode from the IR
4. **Class Loading**: Load the generated bytecode into the JVM

Here's a high-level design:

```kotlin
class JITCompiler {
    companion object {
        private val compiledStatements = ConcurrentHashMap<String, Class<*>>()
        
        fun isCompiled(statementId: String): Boolean = compiledStatements.containsKey(statementId)
        
        fun scheduleCompilation(statementId: String) {
            if (compiledStatements.containsKey(statementId)) return
            
            CompilationWorker.submit {
                val compiledClass = compileStatement(statementId)
                compiledStatements[statementId] = compiledClass
            }
        }
        
        private fun compileStatement(statementId: String): Class<*> {
            val ast = StatementRegistry.getAst(statementId)
            val ir = IRGenerator.generate(ast)
            OptimizationPipeline.optimize(ir)
            val bytecode = BytecodeGenerator.generate(ir)
            return ByteArrayClassLoader.loadClass(bytecode)
        }
    }
}
```

#### 5.3 Integration with InternalInterpreter

To integrate JIT compilation with the existing interpreter, modify the statement execution logic:

```kotlin
override fun executeStatement(statement: Statement): Value {
    val statementId = getStatementId(statement)
    
    // Check if statement is already compiled
    JITCompiler.getCompiledStatement(statementId)?.let { compiledStatement ->
        return executeCompiled(compiledStatement, getCurrentContext())
    }
    
    // Record execution for hot spot detection
    HotSpotTracker.recordExecution(statementId)
    
    // Fall back to interpreted execution
    return interpretStatement(statement)
}

private fun executeCompiled(compiledStatement: Class<*>, context: ExecutionContext): Value {
    val instance = compiledStatement.getDeclaredConstructor().newInstance()
    val executeMethod = compiledStatement.getMethod("execute", ExecutionContext::class.java)
    return executeMethod.invoke(instance, context) as Value
}
```

#### 5.4 Leveraging Existing AST for JIT Compilation

Instead of creating a separate IR hierarchy, the JIT compilation system should leverage Jariko's existing AST classes from the `com.smeup.rpgparser.parsing.ast` package, which already use the Kolasu node model. This approach has several advantages:

1. **Reuse established code**: Avoids duplicating the already well-defined language constructs
2. **Reduce complexity**: Maintains a single representation of the program
3. **Simplify maintenance**: Changes to language features only need to be implemented once
4. **Reduce memory footprint**: Avoids storing multiple representations of the same program

##### 5.4.1 AST Annotation for JIT Compilation

Rather than transforming to a new IR, add JIT-specific metadata to the existing AST nodes:

```kotlin
/**
 * Interface to be implemented by AST nodes that can be JIT-compiled
 */
interface JitCompilable {
    // Compilation state (NOT_COMPILED, COMPILING, COMPILED, FAILED)
    var compilationStatus: CompilationStatus
    
    // Execution frequency tracking
    var executionCount: Int
    
    // Bytecode generation and caching
    fun generateBytecode(methodVisitor: MethodVisitor, context: BytecodeGenerationContext)
    
    // Optimization hints that can be set by optimization passes
    val optimizationHints: MutableMap<String, Any>
}
```

Extend the existing AST nodes to support JIT compilation:

```kotlin
// Example extension of an existing AST class
class JitEnhancedStatement(val originalStatement: Statement) : JitCompilable {
    override var compilationStatus = CompilationStatus.NOT_COMPILED
    override var executionCount = 0
    override val optimizationHints = mutableMapOf<String, Any>()

    override fun generateBytecode(methodVisitor: MethodVisitor, context: BytecodeGenerationContext) {
        when (originalStatement) {
            is AssignmentStatement -> generateAssignmentBytecode(originalStatement, methodVisitor, context)
            is IfStatement -> generateIfBytecode(originalStatement, methodVisitor, context)
            // Other statement types
        }
    }
}
```

##### 5.4.2 Bytecode Generation Context

Create a context class to maintain state during bytecode generation:

```kotlin
class BytecodeGenerationContext(
    val symbolTable: ISymbolTable,
    val interpreter: InterpreterCore,
    val classWriter: ClassWriter
) {
    // Track local variable indices
    private val localVariables = mutableMapOf<String, Int>()
    private var nextLocalVariableIndex = 1  // 0 is reserved for 'this'
    
    // Get or allocate a local variable slot
    fun getLocalVariableIndex(name: String): Int {
        return localVariables.getOrPut(name) { nextLocalVariableIndex++ }
    }
    
    // Get JVM type for RPG type
    fun getRpgTypeDescriptor(type: Type): String {
        return when (type) {
            is StringType -> "Ljava/lang/String;"
            is NumberType -> {
                if (type.integer) "I" else "Ljava/math/BigDecimal;"
            }
            // Handle other types
            else -> "Ljava/lang/Object;"
        }
    }
    
    // Helper methods for common bytecode patterns
    fun loadSymbol(name: String, mv: MethodVisitor) {
        val localIndex = getLocalVariableIndex(name)
        mv.visitVarInsn(Opcodes.ALOAD, localIndex)
    }
    
    fun storeSymbol(name: String, mv: MethodVisitor) {
        val localIndex = getLocalVariableIndex(name)
        mv.visitVarInsn(Opcodes.ASTORE, localIndex)
    }
}
```

##### 5.4.3 Direct Bytecode Generation Examples

Generate bytecode directly from existing AST nodes:

```kotlin
/**
 * Generate bytecode for an assignment statement
 */
fun generateAssignmentBytecode(stmt: AssignmentStatement, mv: MethodVisitor, context: BytecodeGenerationContext) {
    // Generate code to evaluate the right-hand side expression
    generateExpressionBytecode(stmt.value, mv, context)
    
    // Store the result in the target variable
    when (val target = stmt.target) {
        is DataRefExpr -> {
            val dataName = target.variable.name
            context.storeSymbol(dataName, mv)
        }
        
        is ArrayAccessExpr -> {
            // Generate array access bytecode
            generateExpressionBytecode(target.array, mv, context)
            generateExpressionBytecode(target.index, mv, context)
            
            // Store element
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "com/smeup/rpgparser/interpreter/ArrayValue",
                "setElement",
                "(ILcom/smeup/rpgparser/interpreter/Value;)V",
                false
            )
        }
        
        // Handle other target types
    }
}

/**
 * Generate bytecode for an if statement
 */
fun generateIfBytecode(stmt: IfStatement, mv: MethodVisitor, context: BytecodeGenerationContext) {
    // Generate condition evaluation
    generateExpressionBytecode(stmt.condition, mv, context)
    
    // Create labels for true/false branches and end
    val falseLabel = Label()
    val endLabel = Label()
    
    // Branch if condition is false
    mv.visitJumpInsn(Opcodes.IFEQ, falseLabel)
    
    // Generate true branch
    stmt.thenBody.forEach { statement ->
        generateStatementBytecode(statement, mv, context)
    }
    
    // Jump to end after executing true branch
    mv.visitJumpInsn(Opcodes.GOTO, endLabel)
    
    // False branch
    mv.visitLabel(falseLabel)
    stmt.elseBody?.forEach { statement ->
        generateStatementBytecode(statement, mv, context)
    }
    
    // End of if statement
    mv.visitLabel(endLabel)
}

/**
 * Generate bytecode for expression evaluation
 */
fun generateExpressionBytecode(expr: Expression, mv: MethodVisitor, context: BytecodeGenerationContext) {
    when (expr) {
        is IntLiteral -> {
            // Push the integer value onto the stack
            mv.visitLdcInsn(expr.value)
            // Wrap it in a Jariko IntValue object
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "com/smeup/rpgparser/interpreter/IntValue",
                "of",
                "(I)Lcom/smeup/rpgparser/interpreter/IntValue;",
                false
            )
        }
        
        is StringLiteral -> {
            // Push the string value onto the stack
            mv.visitLdcInsn(expr.value)
            // Create a Jariko StringValue
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "com/smeup/rpgparser/interpreter/StringValue",
                "of",
                "(Ljava/lang/String;)Lcom/smeup/rpgparser/interpreter/StringValue;",
                false
            )
        }
        
        is DataRefExpr -> {
            // Load variable value from local variable slot or interpreter
            val varName = expr.variable.name
            if (context.localVariables.containsKey(varName)) {
                // Variable is in a local slot
                context.loadSymbol(varName, mv)
            } else {
                // Load from interpreter's symbol table
                mv.visitVarInsn(Opcodes.ALOAD, 0) // Load 'this'
                mv.visitFieldInsn(
                    Opcodes.GETFIELD,
                    "CompiledStatement",
                    "interpreter",
                    "Lcom/smeup/rpgparser/interpreter/InterpreterCore;"
                )
                mv.visitLdcInsn(varName)
                mv.visitMethodInsn(
                    Opcodes.INVOKEINTERFACE,
                    "com/smeup/rpgparser/interpreter/InterpreterCore",
                    "get",
                    "(Ljava/lang/String;)Lcom/smeup/rpgparser/interpreter/Value;",
                    true
                )
            }
        }
        
        is BinaryExpression -> {
            // Evaluate left and right operands
            generateExpressionBytecode(expr.left, mv, context)
            generateExpressionBytecode(expr.right, mv, context)
            
            // Apply the operation based on operator type
            when (expr.operation) {
                // Arithmetic operations
                BinaryOperation.PLUS -> generateBinaryOp(mv, "plus")
                BinaryOperation.MINUS -> generateBinaryOp(mv, "minus")
                BinaryOperation.MULT -> generateBinaryOp(mv, "times")
                BinaryOperation.DIV -> generateBinaryOp(mv, "div")
                
                // Comparison operations
                BinaryOperation.EQ -> generateComparisonOp(mv, "eq")
                BinaryOperation.NE -> generateComparisonOp(mv, "ne")
                BinaryOperation.GT -> generateComparisonOp(mv, "gt")
                BinaryOperation.GE -> generateComparisonOp(mv, "ge")
                BinaryOperation.LT -> generateComparisonOp(mv, "lt")
                BinaryOperation.LE -> generateComparisonOp(mv, "le")
                
                // Logical operations
                BinaryOperation.AND -> generateLogicalOp(mv, "and")
                BinaryOperation.OR -> generateLogicalOp(mv, "or")
            }
        }
        
        is UnaryExpression -> {
            // Evaluate the operand
            generateExpressionBytecode(expr.operand, mv, context)
            
            // Apply the unary operation
            when (expr.operation) {
                UnaryOperation.NEGATE -> {
                    mv.visitMethodInsn(
                        Opcodes.INVOKEVIRTUAL,
                        "com/smeup/rpgparser/interpreter/Value",
                        "negate",
                        "()Lcom/smeup/rpgparser/interpreter/Value;",
                        false
                    )
                }
                UnaryOperation.NOT -> {
                    mv.visitMethodInsn(
                        Opcodes.INVOKEVIRTUAL,
                        "com/smeup/rpgparser/interpreter/Value",
                        "not",
                        "()Lcom/smeup/rpgparser/interpreter/Value;",
                        false
                    )
                }
            }
        }
        
        is FunctionCall -> {
            // Handle function calls
            val functionName = expr.function.name
            
            // Push function arguments onto the stack
            expr.args.forEach { arg -> 
                generateExpressionBytecode(arg, mv, context) 
            }
            
            // Create array of arguments
            mv.visitLdcInsn(expr.args.size)
            mv.visitTypeInsn(Opcodes.ANEWARRAY, "com/smeup/rpgparser/interpreter/Value")
            
            // Store each argument in the array
            for (i in expr.args.indices) {
                mv.visitInsn(Opcodes.DUP)         // Duplicate array reference
                mv.visitLdcInsn(i)                // Push array index
                mv.visitInsn(Opcodes.SWAP)        // Swap value and index
                mv.visitInsn(Opcodes.AASTORE)     // Store in array
            }
            
            // Get system interface
            mv.visitVarInsn(Opcodes.ALOAD, 0)     // Load 'this'
            mv.visitFieldInsn(
                Opcodes.GETFIELD,
                "CompiledStatement",
                "systemInterface",
                "Lcom/smeup/rpgparser/interpreter/SystemInterface;"
            )
            
            // Call the function
            mv.visitLdcInsn(functionName)
            mv.visitMethodInsn(
                Opcodes.INVOKEINTERFACE,
                "com/smeup/rpgparser/interpreter/SystemInterface",
                "callFunction", 
                "(Ljava/lang/String;[Lcom/smeup/rpgparser/interpreter/Value;)Lcom/smeup/rpgparser/interpreter/Value;",
                true
            )
        }
        
        is ArrayAccessExpr -> {
            // Generate array reference
            generateExpressionBytecode(expr.array, mv, context)
            
            // Generate index expression
            generateExpressionBytecode(expr.index, mv, context)
            
            // Convert index to int
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "com/smeup/rpgparser/interpreter/Value",
                "asInt",
                "()I",
                false
            )
            
            // Get array element
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "com/smeup/rpgparser/interpreter/ArrayValue",
                "getElement",
                "(I)Lcom/smeup/rpgparser/interpreter/Value;",
                false
            )
        }
        
        // Handle other expression types as needed
        else -> {
            // Fallback: generate code to call the interpreter for evaluation
            mv.visitVarInsn(Opcodes.ALOAD, 0) // Load 'this'
            mv.visitFieldInsn(
                Opcodes.GETFIELD,
                "CompiledStatement",
                "interpreter",
                "Lcom/smeup/rpgparser/interpreter/InterpreterCore;"
            )
            
            // Create Expression object - this would require some serialization mechanism
            // For simplicity, we'll assume there's a registry of expressions
            val exprId = ExpressionRegistry.registerExpression(expr)
            mv.visitLdcInsn(exprId)
            
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "com/smeup/rpgparser/jit/ExpressionRegistry",
                "getExpression",
                "(Ljava/lang/String;)Lcom/smeup/rpgparser/parsing/ast/Expression;",
                false
            )
            
            // Evaluate the expression using the interpreter
            mv.visitMethodInsn(
                Opcodes.INVOKEINTERFACE,
                "com/smeup/rpgparser/interpreter/InterpreterCore",
                "eval",
                "(Lcom/smeup/rpgparser/parsing/ast/Expression;)Lcom/smeup/rpgparser/interpreter/Value;",
                true
            )
        }
    }
}

/**
 * Helper method to generate bytecode for binary arithmetic operations
 */
private fun generateBinaryOp(mv: MethodVisitor, methodName: String) {
    mv.visitMethodInsn(
        Opcodes.INVOKEVIRTUAL,
        "com/smeup/rpgparser/interpreter/Value",
        methodName,
        "(Lcom/smeup/rpgparser/interpreter/Value;)Lcom/smeup/rpgparser/interpreter/Value;",
        false
    )
}

/**
 * Helper method to generate bytecode for comparison operations
 */
private fun generateComparisonOp(mv: MethodVisitor, methodName: String) {
    mv.visitMethodInsn(
        Opcodes.INVOKEVIRTUAL,
        "com/smeup/rpgparser/interpreter/Value",
        methodName,
        "(Lcom/smeup/rpgparser/interpreter/Value;)Lcom/smeup/rpgparser/interpreter/BooleanValue;",
        false
    )
}

/**
 * Helper method to generate bytecode for logical operations
 */
private fun generateLogicalOp(mv: MethodVisitor, methodName: String) {
    mv.visitMethodInsn(
        Opcodes.INVOKEVIRTUAL,
        "com/smeup/rpgparser/interpreter/BooleanValue",
        methodName,
        "(Lcom/smeup/rpgparser/interpreter/Value;)Lcom/smeup/rpgparser/interpreter/BooleanValue;",
        false
    )
}

/**
 * Generate bytecode for a statement by delegating to appropriate generator
 */
private fun generateStatementBytecode(statement: Statement, mv: MethodVisitor, context: BytecodeGenerationContext) {
    when (statement) {
        is AssignmentStatement -> generateAssignmentBytecode(statement, mv, context)
        is IfStatement -> generateIfBytecode(statement, mv, context)
        // Add handlers for other statement types as needed
        else -> {
            // Fallback for statements we don't handle yet - use interpreter
            mv.visitVarInsn(Opcodes.ALOAD, 0)  // Load 'this'
            mv.visitFieldInsn(
                Opcodes.GETFIELD, 
                "CompiledStatement",
                "interpreter",
                "Lcom/smeup/rpgparser/interpreter/InterpreterCore;"
            )
            
            // Create Statement object - this would require some serialization mechanism
            // For simplicity, we'll assume there's a registry of statements
            val stmtId = StatementRegistry.registerStatement(statement)
            mv.visitLdcInsn(stmtId)
            
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "com/smeup/rpgparser/jit/StatementRegistry",
                "getStatement",
                "(Ljava/lang/String;)Lcom/smeup/rpgparser/parsing/ast/Statement;",
                false
            )
            
            // Execute the statement using the interpreter
            mv.visitMethodInsn(
                Opcodes.INVOKEINTERFACE,
                "com/smeup/rpgparser/interpreter/InterpreterCore",
                "execute",
                "(Lcom/smeup/rpgparser/parsing/ast/Statement;)V",
                true
            )
        }
    }
}
```
<|diff_marker|>
