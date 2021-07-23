#AST serialization

In this document, we are going to explain the **AST serialization**.

The need to serialize the AST (Abstract Syntax Tree) derives after realizing that the bottleneck was at the creation  of the ParseTree from RPG source.  

Consequently, it was decided to use serialization made avaiable from Kotlin.  

Then, the **@Serializable** annotation was applied to all abstract and concrete classes involved in the AST.  

After serializing the ***CompilationUnit***, you get the serialized AST encoded in bytes or in json.  
At the moment Jariko has implemented serialization only in Bytes.