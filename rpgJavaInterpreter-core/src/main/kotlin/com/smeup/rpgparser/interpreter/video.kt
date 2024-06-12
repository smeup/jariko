package com.smeup.rpgparser.interpreter

import com.smeup.dspfparser.linesclassifier.DSPF
import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType

internal fun DSPF.getDbFields(): List<DbField> {
    val fields = mutableListOf<DbField>()

    records.forEach { record ->
        record.fields.forEach { field ->
            val type: Type
            val rpgType: RpgType
            // currently parser can't handle REFFLD, so I created random fallback values
            val fallbackLength = 10
            val fallbackPrecision = 10

            if (field.isNumeric) {
                if (field.precision!! > 0) rpgType = RpgType.ZONED
                else rpgType = RpgType.INTEGER

                type = NumberType(
                    field.length ?: fallbackLength,
                    field.precision ?: fallbackPrecision, rpgType
                )
            } else {
                type = StringType.createInstance(field.length ?: fallbackLength)
            }
            fields.add(DbField(fieldName = field.name, type = type))
        }
    }

    return fields
}

internal fun List<FileDefinition>.toDSPF(): Map<String, DSPF>? {
    val displayFiles = mutableMapOf<String, DSPF>()
    val dspfConfig = MainExecutionContext.getConfiguration().dspfConfig

    this.filter { it.fileType == FileType.VIDEO }.forEach {
        if (dspfConfig != null) {
            displayFiles[it.name] = dspfConfig.dspfProducer.invoke(it.name)
        }
        // if dspfConfig == null then display file fields have already been loaded into
        // data definitions from .json metadata (reloadConfig is used as fallback)
    }

    // should I return null or an empty map?
    return if (dspfConfig != null) displayFiles else null
}

internal fun loadDSPFFields(interpreter: InterpreterCore, formatName: String): List<DSPFField> {
    val fields = mutableListOf<DSPFField>()
    val symbolTable = interpreter.getGlobalSymbolTable()
    val displayFiles = interpreter.getStatus().displayFiles

    displayFiles?.forEach { dspf ->
        val record = dspf.value.records.first { it.name == formatName }
        record.fields.forEach { field ->
            field.value.primitive = symbolTable[field.name].asString().value
            fields.add(field)
        }
    }

    return fields
}

internal fun unloadDSPFFields(interpreter: InterpreterCore, response: OnExfmtResponse) {
    val symbolTable = interpreter.getGlobalSymbolTable()

    response.values.forEach { field ->
        val dataDefinition = symbolTable.dataDefinitionByName(field.key)
        dataDefinition ?: error("Data definition ${field.key} does not exists in symbol table")
        when (dataDefinition.type) {
            is StringType -> symbolTable[dataDefinition] = StringValue(field.value)
            is NumberType -> symbolTable[dataDefinition] = DecimalValue(field.value.toBigDecimal())
            else -> error("Unhandled data type")
        }

    }
}