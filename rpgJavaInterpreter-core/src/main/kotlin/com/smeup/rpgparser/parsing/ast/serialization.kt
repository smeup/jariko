package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.parsing.parsetreetoast.LogicalCondition
import kotlinx.serialization.*
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

private val modules = SerializersModule {
    polymorphic(Statement::class) {
        subclass(AddStmt::class)
        subclass(CabStmt::class)
        subclass(CallStmt::class)
        subclass(CatStmt::class)
        subclass(ChainStmt::class)
        subclass(CheckStmt::class)
        subclass(ClearStmt::class)
        subclass(CompStmt::class)
        subclass(DefineStmt::class)
        subclass(DisplayStmt::class)
        subclass(DoStmt::class)
        subclass(DouStmt::class)
        subclass(DowStmt::class)
        subclass(EvalStmt::class)
        subclass(ExecuteSubroutine::class)
        subclass(ForStmt::class)
        subclass(GotoStmt::class)
        subclass(IfStmt::class)
        subclass(IterStmt::class)
        subclass(KListStmt::class)
        subclass(LeaveSrStmt::class)
        subclass(LeaveStmt::class)
        subclass(LookupStmt::class)
        subclass(MoveAStmt::class)
        subclass(MoveLStmt::class)
        subclass(MoveStmt::class)
        subclass(MultStmt::class)
        subclass(OtherStmt::class)
        subclass(PlistStmt::class)
        subclass(ReadEqualStmt::class)
        subclass(ReadPreviousStmt::class)
        subclass(ReadStmt::class)
        subclass(ReturnStmt::class)
        subclass(ScanStmt::class)
        subclass(SelectStmt::class)
        subclass(SetStmt::class)
        subclass(SetllStmt::class)
        subclass(SortAStmt::class)
        subclass(SubDurStmt::class)
        subclass(SubStmt::class)
        subclass(TagStmt::class)
        subclass(TimeStmt::class)
        subclass(XFootStmt::class)
        subclass(ZAddStmt::class)
        subclass(ZSubStmt::class)
    }
    polymorphic(Expression::class) {
        subclass(AbsExpr::class)
        subclass(AllExpr::class)
        subclass(ArrayAccessExpr::class)
        subclass(AssignableExpression::class)
        subclass(AssignmentExpr::class)
        subclass(BlanksRefExpr::class)
        subclass(CharExpr::class)
        subclass(DataRefExpr::class)
        subclass(DataWrapUpIndicatorExpr::class)
        subclass(DecExpr::class)
        subclass(DiffExpr::class)
        subclass(DifferentThanExpr::class)
        subclass(DivExpr::class)
        subclass(EditcExpr::class)
        subclass(EditwExpr::class)
        subclass(EofExpr::class)
        subclass(EqualExpr::class)
        subclass(EqualityExpr::class)
        subclass(ExpExpr::class)
        subclass(FigurativeConstantRef::class)
        subclass(FoundExpr::class)
        subclass(FunctionCall::class)
        subclass(GreaterEqualThanExpr::class)
        subclass(GreaterThanExpr::class)
        subclass(HiValExpr::class)
        subclass(IntExpr::class)
        subclass(IntLiteral::class)
        subclass(LenExpr::class)
        subclass(LessEqualThanExpr::class)
        subclass(LessThanExpr::class)
        subclass(LogicalAndExpr::class)
        subclass(LogicalCondition::class)
        subclass(LogicalOrExpr::class)
        subclass(LookupExpr::class)
        subclass(LowValExpr::class)
        subclass(MinusExpr::class)
        subclass(MultExpr::class)
        subclass(NotExpr::class)
        subclass(NumberLiteral::class)
        subclass(NumberOfElementsExpr::class)
        subclass(OffRefExpr::class)
        subclass(OnRefExpr::class)
        subclass(PlusExpr::class)
        subclass(PredefinedGlobalIndicatorExpr::class)
        subclass(PredefinedIndicatorExpr::class)
        subclass(QualifiedAccessExpr::class)
        subclass(RealLiteral::class)
        subclass(RemExpr::class)
        subclass(ReplaceExpr::class)
        subclass(ScanExpr::class)
        subclass(SqrtExpr::class)
        subclass(StringLiteral::class)
        subclass(SubstExpr::class)
        subclass(TimeStampExpr::class)
        subclass(TranslateExpr::class)
        subclass(TrimExpr::class)
        subclass(TrimlExpr::class)
        subclass(TrimrExpr::class)
        subclass(ZeroExpr::class)
    }
    polymorphic(AssignableExpression::class) {
        subclass(ArrayAccessExpr::class)
        subclass(DataRefExpr::class)
        subclass(DataWrapUpIndicatorExpr::class)
        subclass(PredefinedGlobalIndicatorExpr::class)
        subclass(PredefinedIndicatorExpr::class)
        subclass(QualifiedAccessExpr::class)
        subclass(SubstExpr::class)

    }
}

private val json = Json {
    serializersModule = modules
}

@ExperimentalSerializationApi
private val cbor = Cbor {
    serializersModule = modules
}

fun CompilationUnit.encodeToString() = json.encodeToString(this)
fun String.createCompilationUnit() = json.decodeFromString<CompilationUnit>(this)

@ExperimentalSerializationApi
fun CompilationUnit.encodeToByteArray() = cbor.encodeToByteArray(this)
@ExperimentalSerializationApi
fun ByteArray.createCompilationUnit() = cbor.decodeFromByteArray<CompilationUnit>(this)
