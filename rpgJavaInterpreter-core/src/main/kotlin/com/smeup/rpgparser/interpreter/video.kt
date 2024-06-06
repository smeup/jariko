package com.smeup.rpgparser.interpreter

import com.smeup.dspfparser.domain.DSPF

internal fun DSPF.getDbFields(): List<DbField> {
    val fields = mutableListOf<DbField>()

    records.forEach { record ->
        record.fields.forEach { field ->
            var type: Type
            var rpgType: String

            if (field.isNumeric) {
                if (field.precision!! > 0) rpgType = "Z"
                else rpgType = "S"

                type = NumberType(field.length!!, field.precision!!, rpgType)
            } else {
                type = StringType.createInstance(field.length!!)
            }
            fields.add(DbField(fieldName = field.name, type = type))
        }
    }

    return fields
}