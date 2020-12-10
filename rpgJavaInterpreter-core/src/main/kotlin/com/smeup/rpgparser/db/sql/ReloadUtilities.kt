package com.smeup.rpgparser.db.sql

import com.smeup.dbnative.file.Record
import com.smeup.dbnative.model.*
import com.smeup.dbnative.model.BooleanType
import com.smeup.dbnative.model.CharacterType
import com.smeup.dbnative.model.TimeStampType
import com.smeup.dbnative.utils.getField
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.interpreter.Type
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType

/**
 * Convert DBNative Field in Jariko DataDefinition
 */
fun Field.toDataDefinition(): DataDefinition {

    val fieldType = this.type

    var type: Type = when (fieldType) {
        is CharacterType -> StringType(fieldType.size, !fieldType.fixedSize)
        is IntegerType -> NumberType(fieldType.size, fieldType.digits, RpgType.INTEGER)
        is VarcharType -> StringType(fieldType.size, !fieldType.fixedSize)
        is SmallintType -> NumberType(fieldType.size, fieldType.digits, RpgType.INTEGER)
        is BigintType -> NumberType(fieldType.size, fieldType.digits, RpgType.INTEGER)
        is BooleanType -> com.smeup.rpgparser.interpreter.BooleanType
        is DecimalType -> NumberType(fieldType.size, fieldType.digits, RpgType.ZONED)
        is FloatType -> NumberType(fieldType.size, fieldType.digits, RpgType.ZONED)
        is DoubleType -> NumberType(fieldType.size, fieldType.digits, RpgType.ZONED)
        is TimeStampType -> com.smeup.rpgparser.interpreter.TimeStampType
        is DateType -> com.smeup.rpgparser.interpreter.TimeStampType
        is TimeType -> com.smeup.rpgparser.interpreter.TimeStampType
        is BinaryType -> NumberType(fieldType.size, fieldType.digits, RpgType.BINARY)
        is VarbinaryType -> NumberType(fieldType.size, fieldType.digits, RpgType.BINARY)
    }

    return DataDefinition(this.name, type)
}

/*
/**
 * Convert Jariko values in corresponding Reload values
 */
public fun Value.asReloadValue(): com.smeup.dbnative.model.Value {

    var value =  when (this) {

        is StringValue -> com.smeup.dbnative.model.StringValue(this.value)
        is IntValue -> com.smeup.dbnative.model.IntValue(this.value.toInt())
        is DecimalValue -> com.smeup.dbnative.model.DecimalValue(this.value)
        is BooleanValue -> com.smeup.dbnative.model.BooleanValue(this.value)
        is CharacterValue -> com.smeup.dbnative.model.StringValue(this.value.toString())
        is ArrayValue -> com.smeup.dbnative.model.StringValue(this.asString().value)
        is ConcreteArrayValue -> com.smeup.dbnative.model.StringValue(this.asString().value)
        is BlanksValue -> com.smeup.dbnative.model.StringValue("")
        is HiValValue -> com.smeup.dbnative.model.StringValue(this.asString().value)
        is LowValValue -> com.smeup.dbnative.model.StringValue(this.asString().value)
        is ZeroValue  -> com.smeup.dbnative.model.IntValue("0".toInt())
        is AllValue -> com.smeup.dbnative.model.StringValue(this.asString().value)
        is ProjectedArrayValue -> com.smeup.dbnative.model.StringValue(this.asString().value)
        else -> com.smeup.dbnative.model.StringValue(this.asString().value)
    }

    return value;
}


/**
 * Convert Jariko values in corresponding Reload values
 */

public fun com.smeup.dbnative.model.Value.asJarikoValue(): Value {
    var value = when (this) {

        is com.smeup.dbnative.model.StringValue -> StringValue(this.value)
        is com.smeup.dbnative.model.IntValue -> IntValue(this.value.toLong())
        is com.smeup.dbnative.model.DecimalValue -> DecimalValue(this.value)
        is com.smeup.dbnative.model.BooleanValue -> BooleanValue(this.value)
        else -> StringValue(this.asString())
    }
    return value
}
*/

fun Record.toValue(metadata: FileMetadata): List<Value> {

    var result = mutableListOf<Value>()

    this.forEach {
        val fieldType = metadata.getField(it.key)?.type

        var value = when (fieldType) {
            is CharacterType -> StringType(fieldType.size, !fieldType.fixedSize)
            is IntegerType -> NumberType(fieldType.size, fieldType.digits, RpgType.INTEGER)
            is VarcharType -> StringType(fieldType.size, !fieldType.fixedSize)
            is SmallintType -> NumberType(fieldType.size, fieldType.digits, RpgType.INTEGER)
            is BigintType -> NumberType(fieldType.size, fieldType.digits, RpgType.INTEGER)
            is BooleanType -> com.smeup.rpgparser.interpreter.BooleanType
            is DecimalType -> NumberType(fieldType.size, fieldType.digits, RpgType.ZONED)
            is FloatType -> NumberType(fieldType.size, fieldType.digits, RpgType.ZONED)
            is DoubleType -> NumberType(fieldType.size, fieldType.digits, RpgType.ZONED)
            is TimeStampType -> com.smeup.rpgparser.interpreter.TimeStampType
            is DateType -> com.smeup.rpgparser.interpreter.TimeStampType
            is TimeType -> com.smeup.rpgparser.interpreter.TimeStampType
            is BinaryType -> NumberType(fieldType.size, fieldType.digits, RpgType.BINARY)
            is VarbinaryType -> NumberType(fieldType.size, fieldType.digits, RpgType.BINARY)
            null -> TODO()
        }
    }

    return result
}