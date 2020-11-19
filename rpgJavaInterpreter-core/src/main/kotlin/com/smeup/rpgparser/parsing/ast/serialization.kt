package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.parsing.parsetreetoast.LogicalCondition
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

private val modules = SerializersModule {
    polymorphic(Statement::class) {
        subclass(DisplayStmt::class)
        subclass(PlistStmt::class)
        subclass(AddStmt::class)
        subclass(SetStmt::class)
        subclass(ClearStmt::class)
        subclass(EvalStmt::class)
        subclass(MoveLStmt::class)
        subclass(TimeStmt::class)
        subclass(ExecuteSubroutine::class)
        subclass(SelectStmt::class)
        subclass(TagStmt::class)
        subclass(SubDurStmt::class)
        subclass(CallStmt::class)
        subclass(IfStmt::class)
        subclass(GotoStmt::class)
        subclass(DoStmt::class)
        subclass(LeaveStmt::class)
        subclass(IterStmt::class)
    }
    polymorphic(Expression::class) {
        subclass(IntLiteral::class)
        subclass(DataRefExpr::class)
        subclass(MinusExpr::class)
        subclass(StringLiteral::class)
        subclass(EqualityExpr::class)
        subclass(SubstExpr::class)
        subclass(DivExpr::class)
        subclass(PlusExpr::class)
        subclass(TrimExpr::class)
        subclass(EditcExpr::class)
        subclass(LogicalOrExpr::class)
        subclass(LogicalCondition::class)
        subclass(HiValExpr::class)
        subclass(DifferentThanExpr::class)
        subclass(LookupExpr::class)
        subclass(IntExpr::class)
        subclass(FunctionCall::class)
        subclass(BlanksRefExpr::class)
        subclass(CharExpr::class)
    }
    polymorphic(AssignableExpression::class) {
        subclass(DataRefExpr::class)
        subclass(DataWrapUpIndicatorExpr::class)
        subclass(ArrayAccessExpr::class)
    }
}

private val json = Json {
    serializersModule = modules
}

private val cbor = Cbor {
    serializersModule = modules
}

fun CompilationUnit.encodeToString() = json.encodeToString(this)
fun String.createCompilationUnit() = json.decodeFromString<CompilationUnit>(this)

fun CompilationUnit.encodeToByteArray() = cbor.encodeToByteArray(this)
fun ByteArray.createCompilationUnit() = cbor.decodeFromByteArray<CompilationUnit>(this)
