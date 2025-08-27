/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * imitations under the License.
 *
 */

package com.smeup.rpgparser.interpreter

import com.smeup.dbnative.file.DBFile
import com.smeup.dspfparser.linesclassifier.DSPF
import com.smeup.rpgparser.parsing.ast.DataRefExpr
import com.smeup.rpgparser.parsing.ast.IndicatorKey
import java.util.HashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class InterpreterStatus(
    val symbolTable: ISymbolTable,
    var indicators: HashMap<IndicatorKey, BooleanValue>,
    var dataAreas: Map<String, String>,
    var returnValue: Value? = null,
    var params: Int = 0,
    var callerParams: Int = params
) {
    var inzsrExecuted: AtomicBoolean = AtomicBoolean(false)
    var lastFound: AtomicBoolean = AtomicBoolean(false)
    var lastDBFile: AtomicReference<DBFile?> = AtomicReference(null)

    var dbFileMap = DBFileMap()
    var displayFiles: Map<String, DSPF>? = null
    var klists: HashMap<String, List<String>> = HashMap<String, List<String>>()

    fun indicator(key: IndicatorKey) = indicators[key] ?: BooleanValue.FALSE

    fun getVar(abstractDataDefinition: AbstractDataDefinition): Value {
        val tmpValue = symbolTable[abstractDataDefinition]
        if (tmpValue is NullValue) {
            throw IllegalArgumentException("Void value for ${abstractDataDefinition.name}")
        }
        return tmpValue
    }

    fun getReferences(pointer: DataRefExpr) = symbolTable.getValues().filter {
        val target = it.key.basedOn as? DataRefExpr
        target?.variable == pointer.variable
    }

    /**
     * Get the data area associated with a data reference.
     *
     * @param dataReference The data reference.
     */
    fun getDataArea(dataReference: String) = dataAreas[dataReference]
}