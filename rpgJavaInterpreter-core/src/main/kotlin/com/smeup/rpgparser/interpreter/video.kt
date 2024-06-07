package com.smeup.rpgparser.interpreter

import com.smeup.dspfparser.domain.DSPF

internal fun DSPF.getDbFields(): List<DbField> {
    val fields = mutableListOf<DbField>()

    records.forEach { record ->
        record.fields.forEach { field ->
            var type: Type
            var rpgType: String
            // currently parser can't handle REFFLD so I created random fallback values
            val fallbackLength = 10
            val fallbackPrecision = 10

            if (field.isNumeric) {
                if (field.precision!! > 0) rpgType = "Z"
                else rpgType = "S"

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