package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.*
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Node

internal fun RpgParser.Dir_apiContext.toApiId(conf: ToAstConfiguration): ApiId {
    return ApiId(
        position = toPosition(conf.considerPosition),
        library = this.library?.text,
        file = this.file?.text,
        member = this.member.text)
}

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

internal fun CompilationUnit.postProcess(): CompilationUnit {
    var compilationUnit = this
    apiDescriptors?.let { apiDescriptors ->
        apiDescriptors.forEach { apiEntry ->
            compilationUnit = when (apiEntry.value.loadApiPolicy) {
                LoadApiPolicy.Always -> compilationUnit.includeApi(apiEntry.key)
                else -> apiEntry.key.todo()
            }
        }
    }
    return compilationUnit
}

private fun <T : Node> List<T>.include(list: List<T>): List<T> {
    return this + list
}

private fun Api.validate(): Api {
    require(compilationUnit.main.stmts.isEmpty()) { "The APIs containing statements are not handled yet" }
    return this
}
