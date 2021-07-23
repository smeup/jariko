#Procedures

In this document, we are going to explain the implementation of **procedure** feature.
The technique used is to consider a procedure as a small program in a large program.

Consequently, we used what Jariko made available for program interpretation: 
the logic of statements and variables, that means using ***InternalInterpreter***.   
This allowed the creation of a new feature without introducing regressions.   
An advantage of the procedures is the possibility of calling other procedures or even the possibility of creating a recursion.

The procedures are represented by the following functions, according to
the language used:
- RPG language: the ***RpgFunction*** function is used.
- JAVA language: the ***JavaFunction*** function is used.

To use the RPG procedures we can use the EVAL opcode, in case we expected a return value, instead CALLP if there is no return value.
While for Java procedures it is necessary to specify in the RPG program the procedure's parameters.

The main steps to create this feature were:
- Creation of the operation code CALLP, developed by analogy of CALL.
- Revision of the SymbolTable.
- Added recovery of the parameters by reference, by value and by reference read-only.

The change made to the symboltable was crucial, because it introduced the SCOPE.
Thanks to SCOPE, it was possible store variables locally in the procedure, but also store global program variables.    

The advantage obtained is the transparency in variables's recovery, because when no variables are found at the local scope, the variables are recovery by Parent symboltable,that is the global scope.