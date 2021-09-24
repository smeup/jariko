# AST serialization

In this document, we are going to explain the **AST serialization**.

Testing RPG sources with many lines of code, the ParseTree's creation from RPG source was identified as the operation with the greatest impact on performance.

Thus it was decided to serialize the AST using the serialization feature made avaiable from Kotlin.  

So, the **@Serializable** annotation was applied to all abstract and concrete classes involved in the AST.  

This way jariko can work with source files (name.rpgle) or serialized AST programs (name.bin), but if you aim for performance you should use the serialized version of your program.