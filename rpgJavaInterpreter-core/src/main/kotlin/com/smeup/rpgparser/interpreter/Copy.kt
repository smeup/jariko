package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.ast.CopyId
import com.smeup.rpgparser.parsing.ast.SourceProgram
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import java.io.InputStream

class Copy(val cu: CompilationUnit, val copyId: CopyId) {
    companion object {
        fun fromInputStream(inputStream: InputStream, copyId: CopyId, sourceProgram: SourceProgram? = SourceProgram.RPGLE): Copy {
            inputStream.use {
                val cu = RpgParserFacade().parseAndProduceAst(inputStream, sourceProgram)
                return Copy(cu, copyId)
            }
        }
    }
}