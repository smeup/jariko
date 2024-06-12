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

/**
 * Fields of specified record will be returned and updated with the latest
 * value of the corrisponding data definition just before EXFMT starts.
 */
internal fun copyDataDefinitionsIntoRecordFields(interpreter: InterpreterCore, recordName: String): List<DSPFField> {
    val fields = mutableListOf<DSPFField>()
    val symbolTable = interpreter.getGlobalSymbolTable()
    val displayFiles = interpreter.getStatus().displayFiles

    displayFiles?.forEach { dspf ->
        val record = dspf.value.records.first { it.name == recordName }
        record.fields.forEach { field ->
            field.value.primitive = symbolTable[field.name].asString().value
            fields.add(field)
        }
    }

    return fields
}

/**
 * Fields edited will during EXFMT will be available just after returning from it as response
 * and used to update corresponding data definitions.
 */
internal fun copyRecordFieldsIntoDataDefinitions(interpreter: InterpreterCore, response: OnExfmtResponse) {
    val symbolTable = interpreter.getGlobalSymbolTable()

    response.values.forEach { field ->
        val dataDefinition = symbolTable.dataDefinitionByName(field.key)
        dataDefinition ?: error("Data definition ${field.key} does not exists in symbol table")

        val isString = runCatching { symbolTable[dataDefinition] = StringValue(field.value) }
        // assignment as decimal is performed before because
        // to a decimal value can't be assigned an integer value
        // but
        // to an integer value can be assigned a decimal value
        val isDecimal = runCatching { symbolTable[dataDefinition] = DecimalValue(field.value.toBigDecimal()) }
        // so if to symbol was already assigned a decimal value this will fail for sure
        val isInt = runCatching { symbolTable[dataDefinition] = IntValue(field.value.toLong()) }

        require(isString.isSuccess || isDecimal.isSuccess || isInt.isSuccess) {
            "Unhandled value type"
        }
    }
}