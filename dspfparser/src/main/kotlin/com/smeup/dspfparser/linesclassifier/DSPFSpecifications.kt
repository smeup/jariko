package com.smeup.dspfparser.linesclassifier

import com.smeup.dspfparser.domain.DSPF
import com.smeup.dspfparser.linesprocessor.DSPFLine
import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFSpecifications(
    override val name: String?
) : DSPF {
    val records: MutableList<DSPFRecordSpecifications> = mutableListOf()

    companion object {
        fun fromLines(
            lines: MutableList<DSPFLine>,
            name: String? = null
        ): DSPFSpecifications {
            return DSPFSpecificationsFactory(name, lines).create()
        }
    }

    override fun getRecord(name: String): DSPFRecordSpecifications {
        return this.records.first { it.name == name.uppercase() }
    }

    override fun getFieldsFromRecord(name: String): MutableList<DSPFFieldSpecifications> {
        return this.records.first { it.name == name.uppercase() }.fields
    }
}

private enum class CurrentContext {
    FILE,
    RECORD,
    FIELD,
}

private class DSPFSpecificationsFactory {
    private var context: CurrentContext = CurrentContext.FILE
    private var isLineNone: Boolean = false
    private var isLineRecord: Boolean = false
    private var isLineField: Boolean = false
    private var result: DSPFSpecifications

    constructor(name: String?, lines: MutableList<DSPFLine>) {
        this.result = DSPFSpecifications(name)
        this.updateResultWith(lines)
    }

    fun create(): DSPFSpecifications {
        return this.result
    }

    private fun tryInsertNewRecord(line: DSPFLine) {
        if (this.isLineRecord) {
            this.result.records.add(DSPFRecordSpecifications.fromLine(line))
            this.context = CurrentContext.RECORD
        }
    }

    private fun tryInsertNewFieldOnRecordContext(line: DSPFLine) {
        if (this.context == CurrentContext.RECORD && this.isLineField) {
            this.result.records.last().fields.add(DSPFFieldSpecifications.fromLine(line))
            this.context = CurrentContext.FIELD
        }
    }

    private fun tryInsertNewFieldOnFieldContext(line: DSPFLine) {
        if (this.context == CurrentContext.FIELD && this.isLineField) {
            this.result.records.last().fields.add(DSPFFieldSpecifications.fromLine(line))
        }
    }

    private fun updateResultWith(lines: MutableList<DSPFLine>) {
        lines.forEach {
            this.isLineNone = it.isNone()
            this.isLineField = it.isField()
            this.isLineRecord = it.isRecord()

            // order is important, do not change it
            this.tryInsertNewRecord(it)
            this.tryInsertNewFieldOnFieldContext(it)
            this.tryInsertNewFieldOnRecordContext(it)
        }
    }
}
