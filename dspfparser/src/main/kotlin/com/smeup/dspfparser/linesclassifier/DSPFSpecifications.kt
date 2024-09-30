package com.smeup.dspfparser.linesclassifier

import com.smeup.dspfparser.linesprocessor.DSPFLine
import com.smeup.dspfparser.linesprocessor.LineType
import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFSpecifications(
    override val records: MutableList<DSPFRecordSpecifications> = mutableListOf()
) : DSPF {
    companion object {
        fun fromLines(
            lines: MutableList<DSPFLine>
        ): DSPFSpecifications {
            return DSPFSpecificationsFactory(lines).create()
        }
    }

    override fun getRecord(name: String): DSPFRecordSpecifications {
        return this.records.first { it.name == name.uppercase() }
    }

    override fun getMutableFieldsFromRecord(name: String): MutableList<MutableField> {
        return this.records.first { it.name == name.uppercase() }.fields
    }

    override fun getConstantFieldsFromRecord(name: String): List<ConstantField> {
        return this.records.first { it.name == name.uppercase() }.constants
    }
}

private enum class CurrentContext {
    FILE,
    RECORD,
    FIELD
}

private class DSPFSpecificationsFactory {
    private var context: CurrentContext = CurrentContext.FILE
    private var currentLineType: LineType = LineType.NULL
    private var result: DSPFSpecifications = DSPFSpecifications()

    constructor(lines: MutableList<DSPFLine>) {
        this.updateResultWith(lines)
    }

    fun create(): DSPFSpecifications {
        return this.result
    }

    private fun tryInsertNewRecord(line: DSPFLine) {
        if (this.currentLineType == LineType.RECORD) {
            this.result.records.add(DSPFRecordSpecifications.fromLine(line))
            this.context = CurrentContext.RECORD
        }
    }

    private fun tryInsertNewFieldOnRecordContext(line: DSPFLine) {
        if (this.context == CurrentContext.RECORD && this.currentLineType == LineType.FIELD) {
            this.result.records.last().fields.add(DSPFFieldSpecifications.fromLine(line) as MutableField)
            this.context = CurrentContext.FIELD
        }
    }

    private fun tryInsertNewFieldOnFieldContext(line: DSPFLine) {
        if (this.context == CurrentContext.FIELD && this.currentLineType == LineType.FIELD) {
            this.result.records.last().fields.add(DSPFFieldSpecifications.fromLine(line) as MutableField)
        }
    }

    private fun tryInsertNewConstantOnFieldContext(line: DSPFLine) {
        if (this.context == CurrentContext.FIELD && this.currentLineType == LineType.CONSTANT) {
            this.result.records.last().constants.add(DSPFFieldSpecifications.fromLine(line) as ConstantField)
        }
    }

    private fun tryInsertNewConstantOnRecordContext(line: DSPFLine) {
        if (this.context == CurrentContext.RECORD && this.currentLineType == LineType.CONSTANT) {
            this.result.records.last().constants.add(DSPFFieldSpecifications.fromLine(line) as ConstantField)
            this.context = CurrentContext.FIELD
        }
    }

    private fun updateResultWith(lines: MutableList<DSPFLine>) {
        lines.forEach {
            this.currentLineType = it.type

            // order is important, do not change it
            this.tryInsertNewRecord(it)
            // try with field context before record context because after matching
            // record context, context is switched to field leading to a double match
            this.tryInsertNewFieldOnFieldContext(it)
            this.tryInsertNewFieldOnRecordContext(it)
            this.tryInsertNewConstantOnFieldContext(it)
            this.tryInsertNewConstantOnRecordContext(it)
        }
    }
}
