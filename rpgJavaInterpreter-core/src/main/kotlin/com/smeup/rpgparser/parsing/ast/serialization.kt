/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.interpreter.AbstractDataDefinition
import com.smeup.rpgparser.interpreter.DataDefinition
import com.smeup.rpgparser.interpreter.FieldDefinition
import com.smeup.rpgparser.parsing.parsetreetoast.LogicalCondition
import com.smeup.rpgparser.serialization.BigDecimalSerializer
import com.smeup.rpgparser.serialization.LocalDateTimeSerializer
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import java.math.BigDecimal
import java.time.LocalDateTime

private val modules = SerializersModule {
    polymorphic(AbstractDataDefinition::class) {
        subclass(FieldDefinition::class)
        subclass(DataDefinition::class)
    }
    polymorphic(Statement::class) {
        subclass(AddStmt::class)
        subclass(CabStmt::class)
        subclass(CallStmt::class)
        subclass(CallPStmt::class)
        subclass(CatStmt::class)
        subclass(ChainStmt::class)
        subclass(CheckStmt::class)
        subclass(ClearStmt::class)
        subclass(CloseStmt::class)
        subclass(CompStmt::class)
        subclass(DefineStmt::class)
        subclass(DeleteStmt::class)
        subclass(DisplayStmt::class)
        subclass(DivStmt::class)
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
        subclass(OccurStmt::class)
        subclass(OpenStmt::class)
        subclass(PlistStmt::class)
        subclass(ReadEqualStmt::class)
        subclass(ReadPreviousStmt::class)
        subclass(ReadPreviousEqualStmt::class)
        subclass(ReadStmt::class)
        subclass(ReturnStmt::class)
        subclass(ScanStmt::class)
        subclass(SelectStmt::class)
        subclass(SetStmt::class)
        subclass(SetllStmt::class)
        subclass(SetgtStmt::class)
        subclass(SortAStmt::class)
        subclass(SubDurStmt::class)
        subclass(SubStmt::class)
        subclass(SubstStmt::class)
        subclass(TagStmt::class)
        subclass(TimeStmt::class)
        subclass(UpdateStmt::class)
        subclass(XFootStmt::class)
        subclass(WriteStmt::class)
        subclass(ZAddStmt::class)
        subclass(ZSubStmt::class)
    }
    polymorphic(Expression::class) {
        subclass(AbsExpr::class)
        subclass(AllExpr::class)
        subclass(ArrayAccessExpr::class)
        subclass(AssignmentExpr::class)
        subclass(BlanksRefExpr::class)
        subclass(CharExpr::class)
        subclass(DataRefExpr::class)
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
        subclass(NumberOfElementsExpr::class)
        subclass(OnRefExpr::class)
        subclass(OffRefExpr::class)
        subclass(PlusExpr::class)
        subclass(GlobalIndicatorExpr::class)
        subclass(IndicatorExpr::class)
        subclass(QualifiedAccessExpr::class)
        subclass(RealLiteral::class)
        subclass(RemExpr::class)
        subclass(ReplaceExpr::class)
        subclass(ScanExpr::class)
        subclass(SqrtExpr::class)
        subclass(StringLiteral::class)
        subclass(SubstExpr::class)
        subclass(SubarrExpr::class)
        subclass(TimeStampExpr::class)
        subclass(TranslateExpr::class)
        subclass(TrimExpr::class)
        subclass(TrimlExpr::class)
        subclass(TrimrExpr::class)
        subclass(ZeroExpr::class)
        subclass(ParmsExpr::class)
        subclass(StatusExpr::class)
    }
    polymorphic(AssignableExpression::class) {
        subclass(ArrayAccessExpr::class)
        subclass(DataRefExpr::class)
        subclass(GlobalIndicatorExpr::class)
        subclass(IndicatorExpr::class)
        subclass(QualifiedAccessExpr::class)
        subclass(SubstExpr::class)
        subclass(SubarrExpr::class)
    }
    polymorphic(Directive::class) {
        subclass(ActivationGroupDirective::class)
        subclass(DeceditDirective::class)
    }
    polymorphic(WithRightIndicators::class) {
        subclass(RightIndicators::class)
    }
    polymorphic(MuteAnnotation::class) {
        subclass(MuteComparisonAnnotation::class)
        subclass(MuteFailAnnotation::class)
        subclass(MuteTimeoutAnnotation::class)
        subclass(MuteTypeAnnotation::class)
    }
    contextual(BigDecimal::class, BigDecimalSerializer)
    contextual(LocalDateTime::class, LocalDateTimeSerializer)
}

val json = Json {
    serializersModule = modules
    // needed to solve json serializing issue due to the change of the CompilationUnit.dataDefinitions property
    // type
    allowStructuredMapKeys = true
}

val cbor = Cbor {
    serializersModule = modules
}

fun CompilationUnit.encodeToString() = json.encodeToString(this)
fun String.createCompilationUnit() = json.decodeFromString<CompilationUnit>(this)

fun CompilationUnit.encodeToByteArray() = cbor.encodeToByteArray(this)
fun ByteArray.createCompilationUnit() = cbor.decodeFromByteArray<CompilationUnit>(this)

enum class SourceProgram(val extension: String, val sourceType: Boolean = true) {
    RPGLE(extension = "rpgle"),
    BINARY(extension = "bin", sourceType = false),
    SQLRPGLE(extension = "sqlrpgle");

    companion object {
        fun getByExtension(extension: String): SourceProgram {
            return values().first {
                it.extension == extension
            }
        }
    }
}
