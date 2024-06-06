package com.smeup.rpgparser.interpreter

import com.smeup.dspfparser.domain.DSPF

internal fun DSPF.getDbFields(): List<DbField> {
    val fields = mutableListOf<DbField>()

    records.forEach { record ->
        record.fields.forEach { field ->
            var type = if (field.isNumeric) NumberType(field.length!!, field.precision!!)
                        else StringType.createInstance(field.length!!)
            fields.add(DbField(fieldName = field.name, type = type))
        }
    }

    return fields
}