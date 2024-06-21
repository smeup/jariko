package com.smeup.rpgparser.execution

import com.smeup.rpgparser.interpreter.AbstractDataDefinition
import com.smeup.rpgparser.interpreter.BooleanValue
import com.smeup.rpgparser.interpreter.CharacterValue
import com.smeup.rpgparser.interpreter.ConcreteArrayValue
import com.smeup.rpgparser.interpreter.DataDefinition
import com.smeup.rpgparser.interpreter.DataStructValue
import com.smeup.rpgparser.interpreter.DecimalValue
import com.smeup.rpgparser.interpreter.ISymbolTable
import com.smeup.rpgparser.interpreter.IntValue
import com.smeup.rpgparser.interpreter.InterpreterCore
import com.smeup.rpgparser.interpreter.OccurableDataStructValue
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot
import com.smeup.rpgparser.interpreter.StringValue
import com.smeup.rpgparser.interpreter.SymbolTable
import com.smeup.rpgparser.interpreter.TimeStampValue
import com.smeup.rpgparser.interpreter.UnlimitedStringValue
import com.smeup.rpgparser.interpreter.Value
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import java.io.File

private val json = Json {
    prettyPrint = true
    allowStructuredMapKeys = true
    serializersModule = SerializersModule {
        polymorphic(ISymbolTable::class) {
            subclass(SymbolTable::class)
        }
        polymorphic(AbstractDataDefinition::class) {
            subclass(DataDefinition::class)
        }
        polymorphic(Value::class) {
            subclass(IntValue::class)
            subclass(DecimalValue::class)
            subclass(StringValue::class)
            subclass(BooleanValue::class)
            subclass(TimeStampValue::class)
            subclass(CharacterValue::class)
            subclass(ConcreteArrayValue::class)
            subclass(DataStructValue::class)
            subclass(OccurableDataStructValue::class)
            subclass(UnlimitedStringValue::class)
        }
    }
}

@Serializable
data class SimpleSnapshotConfig(var snapshotPath: String? = null) {

    fun save(runtimeInterpreterSnapshot: RuntimeInterpreterSnapshot, interpreter: InterpreterCore) {
        val snapshotFile = "uuid"
        val file = File(snapshotPath, "$snapshotFile.json")

        file.bufferedWriter().use {
            it.write(json.encodeToString(interpreter.getGlobalSymbolTable()))
        }
    }

    fun restore(runtimeInterpreterSnapshot: RuntimeInterpreterSnapshot) {
        val snapshotFile = "uuid"
        val file = File(snapshotPath, "$snapshotFile.json")

        file.bufferedReader().use {
            //
        }
    }
}