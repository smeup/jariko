package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.ApiDescriptor
import com.smeup.rpgparser.parsing.ast.ApiId

internal fun RpgParser.Dir_apiContext.toApiId(): ApiId {
    return ApiId(library = this.library?.text, file = this.file?.text, member = this.member.text)
}

internal fun List<RpgParser.StatementContext>.toApiDescriptors(conf: ToAstConfiguration): Map<ApiId, ApiDescriptor> {
    return mutableMapOf<ApiId, ApiDescriptor>().let { apiDescriptors ->
        forEach { statementContext ->
            statementContext.runParserRuleContext(conf = conf) { statementContext ->
                statementContext?.directive()?.dir_api()?.let { dirApicontext ->
                    val apiId = dirApicontext.toApiId()
                    apiDescriptors[apiId] = MainExecutionContext.getSystemInterface()!!.findApiDescriptor(apiId)
                }
            }
        }
        apiDescriptors
    }
}

/*
internal fun CompilationUnit.postProcess(): CompilationUnit {
   return apiDescriptors?.let { apiDescriptors ->
       apiDescriptors.forEach() { apiDescriptor ->
           when (apiDescriptor.value.loadApiPolicy) {

           }
       }
   } :? this
}*/
