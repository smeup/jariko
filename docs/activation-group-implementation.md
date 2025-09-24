# Activation Group Implementation Documentation

This document provides comprehensive information about the Activation Group implementation in Jariko to help developers understand the architecture and debug issues.

## Overview

Activation Groups in Jariko represent the runtime context where RPG programs execute, similar to IBM i RPG activation groups. They manage program lifecycle, variable scope, and memory isolation between programs.

## Core Components

### 1. ActivationGroup Data Class
- **Location**: `rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/interpreter/program.kt:269-272`
- **Purpose**: Models the activation group concept
- **Properties**:
  - `type: ActivationGroupType` - The type of activation group (caller, new, named)
  - `assignedName: String` - The actual name assigned to this activation group

```kotlin
data class ActivationGroup(
    val type: ActivationGroupType,
    val assignedName: String,
)
```

### 2. ActivationGroupType Hierarchy
- **Location**: `rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/parsing/ast/directives.kt:37-49`
- **Types**:
  - `CallerActivationGroup`: Inherits caller's activation group
  - `NewActivationGroup`: Creates a new unique activation group
  - `NamedActivationGroup`: Uses a specific named activation group

### 3. ActivationGroupDirective
- **Location**: `rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/parsing/ast/directives.kt:32-35`
- **Purpose**: AST node representing the `H ACTGRP(...)` directive

## Activation Group Assignment Algorithm

The assignment of activation group names follows this algorithm in `interpretation_utils.kt:68-76`:

```kotlin
fun ActivationGroupType.assignedName(caller: RpgProgram?): String =
    when (this) {
        is CallerActivationGroup -> {
            require(caller != null) { "caller is mandatory" }
            caller.activationGroup.assignedName
        }
        is NewActivationGroup -> UUID.randomUUID().toString()
        is NamedActivationGroup -> groupName
    }
```

### Assignment Rules:
1. **CallerActivationGroup**: Uses the same assigned name as the calling program
2. **NewActivationGroup**: Generates a unique UUID string
3. **NamedActivationGroup**: Uses the literal group name from the directive

## Program Execution Flow

### Initialization Process (`program.kt:176-213`)

1. **Determine Activation Group Type**:
   - Check if program has explicit `ACTGRP` directive
   - Handle special case: `ACTGRP(*CALLER)` with no caller defaults to configuration default
   - Programs without directive: main programs use config default, called programs use `*CALLER`

2. **Create Activation Group Instance**:
   ```kotlin
   activationGroup = ActivationGroup(activationGroupType, activationGroupType.assignedName(caller))
   ```

3. **Callback Integration**:
   - Call `jarikoCallback.getActivationGroup()` to allow override
   - Parameters: program name, associated activation group
   - Return null to use default behavior, or return custom ActivationGroup

### Configuration Integration

- **Default Name**: `DEFAULT_ACTIVATION_GROUP_NAME = "*DFTACTGRP"`
- **Configuration Property**: `Configuration.defaultActivationGroupName`
- **Override**: Can be customized via `Configuration` constructor

## Key Implementation Details

### Program Stack Management
- Activation groups are associated with programs in the execution stack
- Located in `internal_interpreter.kt:1212-1223`
- Uses `MainExecutionContext.getProgramStack()` to access current program

### Callback Override Mechanism
- `JarikoCallback.getActivationGroup` allows runtime customization
- Called during program initialization with current activation group context
- Enables external activation group management and monitoring

### Symbol Table Integration
- Each activation group maintains its own symbol table scope
- Managed by `InternalInterpreter`
- Handles variable lifecycle and cleanup

## Testing

### Test Coverage
- **Location**: `rpgJavaInterpreter-core/src/test/kotlin/com/smeup/rpgparser/interpreter/ActivationGroupTest.kt`
- **Test Cases**:
  1. `testMainUnspecified`: Default activation group for main programs
  2. `testUnspecifiedCaller`: `ACTGRP(*CALLER)` behavior
  3. `testMainSpecified`: Named activation group handling
  4. `testCallCaller`: Caller activation group inheritance

### Example Test Program
```rpg
H ACTGRP('MYACT')
C                   SETON                                          RT
```

## Troubleshooting Guide

### Common Issues

#### 1. "caller is mandatory" Error
- **Cause**: `ACTGRP(*CALLER)` used when no caller exists
- **Location**: `interpretation_utils.kt:71`
- **Solution**: Use named activation group or default for main programs
- **Debug**: Check program stack size in `MainExecutionContext`

#### 2. Activation Group Not Inherited Correctly
- **Check**: Verify `CallerActivationGroup` assignment logic
- **Debug Steps**:
  1. Examine program stack: `MainExecutionContext.getProgramStack()`
  2. Verify caller's activation group: `caller.activationGroup.assignedName`
  3. Check callback override: `jarikoCallback.getActivationGroup`

#### 3. Symbol Table Isolation Problems
- **Symptoms**: Variables shared between activation groups unexpectedly
- **Investigation**:
  1. Verify activation group names are different: `activationGroup.assignedName`
  2. Check symbol table creation in `InternalInterpreter`
  3. Examine memory slice storage configuration

#### 4. Configuration Override Not Working
- **Verify**: `Configuration.defaultActivationGroupName` setting
- **Check**: Callback implementation in `JarikoCallback.getActivationGroup`
- **Debug**: Add logging in callback to trace activation group assignment

### Debugging Techniques

#### 1. Enable Debug Logging
```kotlin
val conf = Configuration(
    jarikoCallback = JarikoCallback(
        getActivationGroup = { programName, associatedActivationGroup ->
            println("Program: $programName")
            println("Activation Group: ${associatedActivationGroup?.assignedName}")
            null // Return null for default behavior
        }
    )
)
```

#### 2. Program Stack Inspection
```kotlin
val stack = MainExecutionContext.getProgramStack()
stack.forEach { program ->
    println("Program: ${program.name}, AG: ${program.activationGroup.assignedName}")
}
```

#### 3. Activation Group Directive Parsing
- Check AST generation: `CompilationUnit.activationGroupType()`
- Verify directive parsing in grammar and parse tree conversion

#### 5. Memory Slice Persistence Error
- **Error**: `persist property not set for these slices: [MemorySlice(...)]`
- **Location**: `symbol_table_storaging.kt:165`
- **Root Cause**: The `persist` property of `MemorySlice` objects is not being set before symbol table serialization

**Debug Steps**:

1. **Check RT/LR Indicator Logic**:
   ```kotlin
   // Verify indicator states in internal_interpreter.kt:1248-1260
   private fun isExitingInRTMode(): Boolean {
       val isLROn = getIndicators()[IndicatorType.LR.name.toIndicatorKey()]?.value
       val isRTOn = getIndicators()[IndicatorType.RT.name.toIndicatorKey()]?.value ?: false
       return isRTOn && isLROn != true
   }
   ```

2. **Verify Memory Slice Assignment**:
   - Check if `doSomethingAfterExecution()` is called properly in `internal_interpreter.kt:1266-1268`
   - Confirm the memory slice exists in `MainExecutionContext.getAttributes()`
   - Verify the activation group name matches between slice creation and retrieval

3. **Investigation Steps**:
   ```kotlin
   // Add debugging in doSomethingAfterExecution()
   val exitingRT = isExitingInRTMode()
   val memorySliceId = getMemorySliceId()
   println("Program: ${getInterpretationContext().currentProgramName}")
   println("Activation Group: ${memorySliceId?.activationGroup}")
   println("Exiting RT: $exitingRT")
   println("Memory Slice exists: ${MainExecutionContext.getAttributes().containsKey(memorySliceId?.getAttributeKey())}")
   ```

**Common Solutions**:

- **Missing RT Indicator**: Ensure programs set RT indicator correctly before exit
- **Activation Group Mismatch**: Verify activation group name consistency between slice creation and retrieval
- **Memory Slice Registration**: Check if memory slice is properly registered in `MainExecutionContext`
- **Program Stack Issues**: Ensure the program is properly pushed to execution stack

**Prevention**:
- Always ensure `IMemorySliceStorage` is configured when using symbol table persistence
- Verify RT/LR indicator logic matches your program's exit strategy
- Add logging to track memory slice lifecycle in development environments

### Performance Considerations

1. **UUID Generation**: `NewActivationGroup` creates UUID strings - monitor for performance impact
2. **Callback Overhead**: Custom `getActivationGroup` callbacks are called for every program
3. **Symbol Table Management**: Each activation group maintains separate symbol table
4. **Memory Slice Storage**: Persistence operations can impact performance with large symbol tables

### Migration Notes

When upgrading or modifying activation group behavior:

1. **Backward Compatibility**: Default behavior should remain unchanged
2. **Callback Interface**: Changes to `JarikoCallback.getActivationGroup` signature require version bump
3. **Test Coverage**: Ensure all activation group types are tested with various program call scenarios

## Related Files

- Core implementation: `interpreter/program.kt`
- Type definitions: `parsing/ast/directives.kt`
- Utility functions: `interpreter/interpretation_utils.kt`
- Configuration: `execution/Configuration.kt`
- Test suite: `interpreter/ActivationGroupTest.kt`
- Integration: `interpreter/internal_interpreter.kt`

This documentation should provide sufficient context for developers to understand, debug, and maintain the activation group implementation in Jariko.