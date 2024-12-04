# `GOTO` Statement

In this document we are going to discuss the implementation of the `GOTO` statement 
from a technical standpoint.

This can be useful to understand why specific design choices were made or help as a guide-line to better
understand the execution flow and how to detect and approach `GOTO` issues during maintenance.

## Assumptions

To properly understand this document we assume:
- You have technical knowledge of Jariko
- You know the base Jariko structure and execution logic 
- You know how GOTOs usually work in programming
- You are familiar with Jariko and RPG common terminology

This document will not go in the specific merit of what is mentioned in the list above.

## How it works

Let's start by describing how the `GOTO` works from a high-level standpoint as defined by the language.
In RPG a `GOTO` works by moving the execution flow to a corresponding `TAG`, 
just like you would intuitively expect in any other common programming language.

Syntax-wise `GOTO`s can redirect to `TAG`s defined:
- with the corresponding `TAG` statement.
- at the end of a subroutine.

`TAG` statements can be defined at any nesting level in `CompositeStatement`s.

### Difference from other languages

There is a limitation in behaviour from other common programming language. 

In RPG a `GOTO` can only redirect to a `TAG`:
- defined in the same subroutine or procedure.
- defined at the top level.

This actually simplifies our design allowing us to only look for `TAG`s in two "scopes" at most.

## Understanding Jariko's execution logic

Jariko executes statements by calling the interpreter method `execute(statements: List<Statement>)`. 

This method knows the list of statements to execute and has the duty of keeping track of the order of the execution 
of statements by their index and dispatch the `statement.execute()` for each of them.

It is invoked mainly in two cases:
- When trying to run the main body (see the method `execute(main: MainBody)`)
- When a `CompositeStatement` needs to execute a subset of statements (for instance, its body).

Therefore, the execution of Jariko statements works by tree-walking. The tree is composed by the ordered list of statements
which, when composite, contain one or more child list of statement.

### The execution state in the call stack

For the above reason calls to `execute(statements: List<Statement>)` appear to be recursive.

For instance in a real scenario, we might call a `DO -> IF -> SELECT` statement stack. 
This would resolve multiple recursive calls making up something that looks like the following:

```
.
└── MainBody
    └── DO
        ├── IF
        ├── IF
        ├── IF
        │   └── SELECT
        ├── IF
        │   └── SELECT
        ├── IF
        └── ...
```

## Problems with the `GOTO` logic

The logic we described arises several problems with the implementation of `GOTO`s.
Here are the main ones:
- `GOTO` can redirect to tags defined at any `CompositeStatement` nesting level.
- `GOTO` execution start where the tag is defined and continues from there, not from the start of its containing scope as common.
- After a `GOTO` ends we must be able to redirect the execution to the calling subroutine.
- When the `TAG` is defined at top level we must redirect the flow there and never fall back to the subroutine containing the `GOTO`. We also must be able to intercept following exceptions in top level.
- A `GOTO` must only redirect the execution without spawning a new call to `execute(statements: List<Statement>)`. Doing this would result in a stack overflow and multiple execution when the same GOTO is called multiple times sequentially.
- If the `TAG` was defined in a `CompositeStatement`, after its execution ends, we must be able to restart execution from the next statement in its parent body.

## The implementation 

The implementation is constrained by the above requirements, solving all of them.

### The control flow

The flow of `GOTO`s is controlled by throwing two different kind of `ControlFlowException`s:
- `GotoException`: the base exception thrown in case of a `GOTO` operation
- `GotoTopLevelException`: the exception thrown when we specifically want to go to a `TAG` defined at top level.

The distinction is needed in order to redirect to the correct scope and setup proper exception flows.

### Exception catching

These two exceptions are caught in two different points:
- In `execute(main: MainBody)` we catch `GotoTopLevelException` recursively. The `GOTO` is executed in CU scope.
- In `ExecuteSubroutine.execute()` we catch `GotoException`s recursively and promote them to `GotoTopLevelException` when needed. The `GOTO` is executed in subroutine scope or dispatched at top level.

### The concept of "unwrapping"

To bypass the limitations of `execute(statements: List<Statement>)` the concept of statement unwrapping has been introduced.
It consists in taking the list of statements and exploding it while keeping track of the offset to the next statement to 
execute and the parent `CompositeStatement` of each statement in the list.

This has many benefit:
- It allows us to find deeply nested `TAG`s.
- It allows us to start execution from an arbitrary index in this list (that basically is the index of the `TAG`).
- It gives us all the information about what are the statements before the `GOTO`. Needed to find `TAG`s defined before the `GOTO`.
- It gives us all the information about the `CompositeStatement` tree containing the `GOTO`. Needed to resume the execution in the parent after the `CompositeStatement` containing the tag finishes its execution.
- It allows us to only spawn a single `execute()` in the interpreter call stack to prevent stack overflows as discussed prior.

Unwrapping is performed only once every time a `GOTO` throws its exception and gets caught.

### Execution of unwrapped statements

Unwrapped statements are executed in the interpreter method `executeUnwrappedAt(unwrappedStatements: List<UnwrappedStatementData>, offset: Int)`.

It has a logic very similar to the `execute(statements: List<Statement>)` but after each statement gets executed it increases the execution index by the offset to the next statement associated with each unwrapped statement.

This is because the list of unwrapped statements contains the reference of each statement in a linear fashion but `CompositeStatement`s
already execute their child on their own. Increasing the index by one instead of the offset would result in duplicate execution.

Eventually each `CompositeStatement` calls the base `execute(statements: List<Statement>)` method, therefore this method has to be
imagined like a middleware to keep the context of `GOTO` calls sane and consistent called only when a `GOTO` exception is caught.
