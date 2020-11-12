package com.smeup.rpgparser.db.sql

import com.smeup.dbnative.file.Record
import com.smeup.dbnative.model.*
import com.smeup.dbnative.model.BooleanType
import com.smeup.dbnative.model.CharacterType
import com.smeup.dbnative.model.TimeStampType
import com.smeup.dbnative.utils.getField
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.interpreter.Type
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType


/**
 * Convert DBNative Field in Jariko DataDefinition
 */
public fun Field.toDataDefinition(): DataDefinition {

    val fieldType = this.type

    var type: Type = when (fieldType) {
        is CharacterType -> StringType(fieldType.size, !fieldType.fixedSize)
        is IntegerType   -> NumberType(fieldType.size, fieldType.digits, RpgType.INTEGER)
        is VarcharType   -> StringType(fieldType.size, !fieldType.fixedSize)
        is SmallintType  -> NumberType(fieldType.size, fieldType.digits, RpgType.INTEGER)
        is BigintType    -> NumberType(fieldType.size, fieldType.digits, RpgType.INTEGER)
        is BooleanType   -> com.smeup.rpgparser.interpreter.BooleanType
        is DecimalType   -> NumberType(fieldType.size, fieldType.digits, RpgType.ZONED)
        is FloatType     -> NumberType(fieldType.size, fieldType.digits, RpgType.ZONED)
        is DoubleType    -> NumberType(fieldType.size, fieldType.digits, RpgType.ZONED)
        is TimeStampType -> com.smeup.rpgparser.interpreter.TimeStampType
        is DateType      -> com.smeup.rpgparser.interpreter.TimeStampType
        is TimeType      -> com.smeup.rpgparser.interpreter.TimeStampType
        is BinaryType    -> NumberType(fieldType.size, fieldType.digits, RpgType.BINARY)
        is VarbinaryType -> NumberType(fieldType.size, fieldType.digits, RpgType.BINARY)
        is NumericType   -> NumberType(fieldType.size, fieldType.digits, RpgType.ZONED)
        is RealType      -> NumberType(fieldType.size, fieldType.digits, RpgType.ZONED)
    }

    return DataDefinition(this.name, type)
}

public fun Record.toValue(metadata: FileMetadata): List<Value> {

    var result = mutableListOf<Value>()

    this.forEach() {
        val fieldType = metadata.getField(it.key)?.type

        var value = when (fieldType) {
            is CharacterType -> StringType(fieldType.size, !fieldType.fixedSize)
            is IntegerType   -> NumberType(fieldType.size, fieldType.digits, RpgType.INTEGER)
            is VarcharType   -> StringType(fieldType.size, !fieldType.fixedSize)
            is SmallintType  -> NumberType(fieldType.size, fieldType.digits, RpgType.INTEGER)
            is BigintType    -> NumberType(fieldType.size, fieldType.digits, RpgType.INTEGER)
            is BooleanType   -> com.smeup.rpgparser.interpreter.BooleanType
            is DecimalType   -> NumberType(fieldType.size, fieldType.digits, RpgType.ZONED)
            is FloatType     -> NumberType(fieldType.size, fieldType.digits, RpgType.ZONED)
            is DoubleType    -> NumberType(fieldType.size, fieldType.digits, RpgType.ZONED)
            is TimeStampType -> com.smeup.rpgparser.interpreter.TimeStampType
            is DateType      -> com.smeup.rpgparser.interpreter.TimeStampType
            is TimeType      -> com.smeup.rpgparser.interpreter.TimeStampType
            is BinaryType    -> NumberType(fieldType.size, fieldType.digits, RpgType.BINARY)
            is VarbinaryType -> NumberType(fieldType.size, fieldType.digits, RpgType.BINARY)
            is NumericType   -> NumberType(fieldType.size, fieldType.digits, RpgType.ZONED)
            is RealType      -> NumberType(fieldType.size, fieldType.digits, RpgType.ZONED)
            null -> TODO()
        }
    }

    return result
}