#AST serialization

In this document, we are going to explain the **AST serialization**.

During test with RPG with many lines of code, we realized that the bottleneck was at the creation  of the ParseTree from RPG source.  

Consequently, it was decided to use serialization made avaiable from Kotlin.  

Then, the **@Serializable** annotation was applied to all abstract and concrete classes involved in the AST.  

This way jariko can work with source files (name.rpgle) or serialized AST programs (name.bin), but if you aim for performance you should use the serialized version of your program.