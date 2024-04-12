# /API

In this document, we are going to explain the implementation of **/API feature**.

**/API** is Jariko's own new directive, an independent entity which can be used by different programs and it is able to include compilation units from other APIs.

It integrates in the CompilationUnit of the program that uses it, the `CompilationUnit` associated at the /API.

This allows Jariko to dynamically import only the things needed, without allocating in memory (SymbolTable) unused code.

This is possible thanks to the `SystemInterface` class, which uses the `findApiDescriptor` function to communicate API information to Jariko, such as the load api policy.

The load api policy can be:
- *Static*, Jariko always loads the API
- *Dynamic*, Jariko loads the API's CompilationUnit, if needed, during AST creation.
- *Just in Time*, Jariko loads the API, if needed, during the program interpretation.

[Notes for developer](api4dev.md)