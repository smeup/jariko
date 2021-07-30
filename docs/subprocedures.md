# Subprocedures

In this document, we are going to explain the implementation of **subprocedures**.
The technique used is to consider a subprocedure as a small program in a larger program.

Thus we used the same logic of statements and variables thet Jariko made available for program interpretation: `InternalInterpreter`.   
This allowed the creation of a new feature without introducing regressions.   
Advantages of using subprocedures:  
* Subprocedures can define their own local variables
* Parameters to a subprocedure can also be passed by value
* Subprocedures can be used as a function
* Subprocedures support recursion

Subprocedures are represented by the following functions, according to
the language used:
- RPG language: the `RpgFunction` function is used.
- JAVA language: the `JavaFunction` function is used.

RPG subprocedures that return a value can be used with EVAL opcode, instead CALLP is used when there is no return value.
For Java subprocedures it is necessary to specify in the RPG program the subprocedure's parameters.

The main steps to create this feature were:
- Creation of the operation code CALLP, developed by analogy to CALL.
- Revision of the SymbolTable.
- Added support for by reference, by value and read-only parameters.

The change made to the SymbolTable was crucial, because it introduced the SCOPE.
Thanks to SCOPE, it was possible to store local variables for the subprocedure, but also store global program variables.    

The advantage obtained is the transparency in variables' retrieval, because when no variables are found in the local scope, the variables are retrieved from Parent SymbolTable, that is the global scope.