#/ API

In this document, we are going to explain the implementation of **/API feature**.

The feature **/API** is Jariko's own new directive, an entity independent which can be used by different programs and it is able to include compilation units from other APIs.

It integrates in the CompilationUnit of the program that uses it, the `CompilationUnit` associated at the /API.

This allows Jariko to dynamically import only the things needed, without allocating in memory (SymbolTable) unused code.

This is possible thanks to the `SystemInterface` class, which uses the `findApiDescriptor` function to communicate API information to Jariko, that is how it is structured and what are the load api policy.

In particular, the load api policy are:
- *Static*, always load the API
- *Dynamic*, loads API's CompilationUnit, just in case the program it needs something found in the API.
- *Just in Time*, load what API needs when the program was interpreted.