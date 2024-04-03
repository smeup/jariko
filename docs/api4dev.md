# Functioning of `/API` directive
## Purpose
This documentation describes the behaviour of `/API` directive, about the implementation until 29th March, 2024. For this reason, this documentation could be obsolete after an update of execution flow.

### Prerequisites
1. read [API docs](https://github.com/smeup/jariko/blob/develop/docs/api.md);
2. see the model's implementation in [`api.kt`](https://github.com/smeup/jariko/blob/develop/rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/parsing/ast/api.kt), into `ast` folder;
3. see the "parse tree to ast" implementation in [`api.kt`](https://github.com/smeup/jariko/blob/develop/rpgJavaInterpreter-core/src/main/kotlin/com/smeup/rpgparser/parsing/parsetreetoast/api.kt), into `parsetreetoast` folder;
4. see the example source code [`APIPGM1.rpgle`](https://github.com/smeup/jariko/blob/develop/rpgJavaInterpreter-core/src/test/resources/APIPGM1.rpgle) that contains a call with directive `/API` to [`APIMATH.rpgle`](https://github.com/smeup/jariko/blob/develop/rpgJavaInterpreter-core/src/test/resources/APIMATH.rpgle).

## Scenario
The analysis was conducted by considering this main source code:
```rpgle
      * Test multiple api inclusion
      * MSG is already defined in APIVARS but does not must cause a conflict
     D  MSG            S             50
      *
      /API APIVARS
      *
     C                   EVAL      MSG = 'Hello world'
     C     MSG           DSPLY
     C                   SETON                                        LR
```

The source code of `APIVARS` is:

```rpgle
      * Api containing only variables definition
     D  MSG            S             50
```
## Execution flow
Like every program, there is the construction of `CompilationUnit` in `fun RContext.toAst(...)` of `misc.kt` firstly. For the `apiDescriptors` attribute, there is a call to `this.statement().toApiDescriptors(...)`.

```kt
return CompilationUnit(
    ...
    apiDescriptors = this.statement().toApiDescriptors(conf),
    ...
)
```

This method in `parsetreetoast` processes every statement and calls `RpgSystem.findApiDescriptor(...)` if founds `API` directive. 

```kt
internal fun List<RpgParser.StatementContext>.toApiDescriptors(conf: ToAstConfiguration): Map<ApiId, ApiDescriptor>? {
    return mutableMapOf<ApiId, ApiDescriptor>().let { apiDescriptors ->
        forEach { statementContext ->
            statementContext.runParserRuleContext(conf = conf) {
                statementContext.directive()?.dir_api()?.let { dirApicontext ->
                    val apiId = dirApicontext.toApiId(conf)
                    apiDescriptors[apiId] = MainExecutionContext.getSystemInterface()!!.findApiDescriptor(apiId)
                }
            }
        }
        if (apiDescriptors.isEmpty()) {
            null
        } else {
            apiDescriptors
        }
    }
}
```

The search is successful if the descriptor is `<nome file>.descriptor.[rpgle|bin]`.

```kt
fun findApiDescriptor(apiId: ApiId): ApiDescriptor {
    programFinders.forEach {
        val apiDescriptor = it.findApiDescriptor(apiId)
        if (apiDescriptor != null) {
            return apiDescriptor
        }
    }
    SINGLETON_RPG_SYSTEM?.let {
        if (this != it && it.programFinders.isNotEmpty()) {
            return it.findApiDescriptor(apiId)
        }
    }
    return ApiDescriptor().apply {
        println("ApiDescriptor for $apiId not found, returning $this".yellow())
    }
}
```

This file doesn't exist in this scenario. For this reason there is an `ApiDescriptor` instance with default values for every attribute.

After the creation of `CompilationUnit` object, there is a call to `postProcess` from `parsetreetoast.kt`.

```kt
internal fun CompilationUnit.postProcess(): CompilationUnit {
    var compilationUnit = this
    apiDescriptors?.let { apiDescriptors ->
        apiDescriptors.forEach { apiEntry ->
            compilationUnit = when (apiEntry.value.loadApiPolicy) {
                LoadApiPolicy.Static -> compilationUnit.includeApi(apiEntry.key)
                else -> apiEntry.key.todo()
            }
        }
    }
    return compilationUnit
}
```

Then `includeApi(...)` searches the file with `RpgSystem.findApi(...)`, from `rpg_system.kt`.

```kt
fun findApi(apiId: ApiId): Api {
    programFinders.forEach {
        val api = it.findApi(apiId)
        if (api != null) {
            return api
        }
    }
    SINGLETON_RPG_SYSTEM?.let {
        if (this != it && it.programFinders.isNotEmpty()) {
            return it.findApi(apiId)
        }
    }
    throw RuntimeException("Api $apiId not found")
}
```

Later, there is a call to `DirRpgProgramFinder.findApi(...)`.

```kt
override fun findApi(apiId: ApiId): Api? {
    val file = apiId.toFile(directory, SourceProgram.RPGLE).takeIf {
        it.exists()
    } ?: apiId.toFile(directory, SourceProgram.BINARY).takeIf {
        it.exists()
    }
    return file?.let {
        val extension = file.name.substringAfterLast(".")
        Api.loadApi(file.inputStream(), SourceProgram.getByExtension(extension))
    }
}
```

If the file was found, `loadApi` creates the `CompilationUnit` object about the source imported by the directive. This is recursive for every inner directive.
At the end of these calls, `includeApi(...)` creates a copy of `CompilationUnit` object with a merge of definitions. Here the iomplementation of this method:

```kt
private fun CompilationUnit.includeApi(apiId: ApiId): CompilationUnit {
    return apiId.runNode {
        MainExecutionContext.setExecutionProgramName(apiId.toString())
        val api = MainExecutionContext.getSystemInterface()!!.findApi(apiId).validate()
        this.copy(
            fileDefinitions = this.fileDefinitions.include(api.compilationUnit.fileDefinitions),
            dataDefinitions = this.dataDefinitions.include(api.compilationUnit.dataDefinitions),
            subroutines = this.subroutines.include(api.compilationUnit.subroutines),
            compileTimeArrays = this.compileTimeArrays.include(api.compilationUnit.compileTimeArrays),
            directives = this.directives.include(api.compilationUnit.directives),
            position = this.position,
            apiDescriptors = api.compilationUnit.apiDescriptors?.let {
                this.apiDescriptors?.plus(it)
            } ?: this.apiDescriptors
        )
    }
}
```

In this case, the main program is going to have the same definition of `MSG`; `include` method doesn't filter anything.

## Final considerations

You could have a right question: where does the filtering take place? The `get` for `allDataDefinitions` returns a filtered list of definitions, without duplication.

```kt
    @Derived
    val allDataDefinitions: List<AbstractDataDefinition>
        get() {
            if (_allDataDefinitions.isEmpty()) {
                _allDataDefinitions.addAll(dataDefinitions)
                dataDefinitions.forEach { it -> it.fields.let { _allDataDefinitions.addAll(it) } }
                _allDataDefinitions.addAll(inStatementsDataDefinitions)
                _allDataDefinitions = _allDataDefinitions.removeDuplicatedDataDefinition().toMutableList()
            }
            return _allDataDefinitions
        }
```