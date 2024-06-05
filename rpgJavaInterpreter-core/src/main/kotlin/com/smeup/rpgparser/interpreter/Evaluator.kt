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

package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.parsetreetoast.LogicalCondition

interface Evaluator {
    fun eval(expression: IntLiteral): Value
    fun eval(expression: RealLiteral): Value
    fun eval(expression: StringLiteral): Value
    fun eval(expression: UDateRefExpr): Value
    fun eval(expression: UYearRefExpr): Value
    fun eval(expression: UMonthRefExpr): Value
    fun eval(expression: UDayRefExpr): Value
    fun eval(expression: NumberOfElementsExpr): Value
    fun eval(expression: DataRefExpr): Value
    fun eval(expression: EqualityExpr): Value
    fun eval(expression: DifferentThanExpr): Value
    fun eval(expression: GreaterThanExpr): Value
    fun eval(expression: GreaterEqualThanExpr): Value
    fun eval(expression: LessEqualThanExpr): Value
    fun eval(expression: LessThanExpr): Value
    fun eval(expression: BlanksRefExpr): BlanksValue
    fun eval(expression: DecExpr): Value
    fun eval(expression: PlusExpr): Value
    fun eval(expression: MinusExpr): Value
    fun eval(expression: MultExpr): Value
    fun eval(expression: CharExpr): Value
    fun eval(expression: LookupExpr): Value
    fun eval(expression: LookupGtExpr): Value
    fun eval(expression: LookupGeExpr): Value
    fun eval(expression: LookupLtExpr): Value
    fun eval(expression: LookupLeExpr): Value
    fun eval(expression: ArrayAccessExpr): Value
    fun eval(expression: HiValExpr): HiValValue
    fun eval(expression: LowValExpr): LowValValue
    fun eval(expression: ZeroExpr): ZeroValue
    fun eval(expression: AllExpr): AllValue
    fun eval(expression: TranslateExpr): Value
    fun eval(expression: LogicalAndExpr): Value
    fun eval(expression: LogicalOrExpr): Value
    fun eval(expression: LogicalCondition): Value
    fun eval(expression: OnRefExpr): BooleanValue
    fun eval(expression: NotExpr): BooleanValue
    fun eval(expression: ScanExpr): Value
    fun eval(expression: SubstExpr): Value
    fun eval(expression: CheckExpr): Value
    fun eval(expression: SubarrExpr): Value
    fun eval(expression: LenExpr): Value
    fun eval(expression: OffRefExpr): BooleanValue
    fun eval(expression: IsoFormatExpr): IsoValue
    fun eval(expression: JulFormatExpr): JulValue
    fun eval(expression: IndicatorExpr): BooleanValue
    fun eval(expression: FunctionCall): Value
    fun eval(expression: TimeStampExpr): Value
    fun eval(expression: EditcExpr): Value
    fun eval(expression: DiffExpr): Value
    fun eval(expression: DivExpr): Value
    fun eval(expression: NegationExpr): Value
    fun eval(expression: ExpExpr): Value
    fun eval(expression: TrimrExpr): Value
    fun eval(expression: TrimlExpr): Value
    fun eval(expression: TrimExpr): StringValue
    fun eval(expression: FoundExpr): Value
    fun eval(expression: EofExpr): Value
    fun eval(expression: EqualExpr): Value
    fun eval(expression: AbsExpr): Value
    fun eval(expression: EditwExpr): Value
    fun eval(expression: IntExpr): Value
    fun eval(expression: InthExpr): Value
    fun eval(expression: RemExpr): Value
    fun eval(expression: QualifiedAccessExpr): Value
    fun eval(expression: ReplaceExpr): Value
    fun eval(expression: SqrtExpr): Value
    fun eval(expression: AssignmentExpr): Value
    fun eval(expression: GlobalIndicatorExpr): Value
    fun eval(expression: ParmsExpr): Value
    fun eval(expression: OpenExpr): Value
    fun eval(expression: SizeExpr): Value
}
