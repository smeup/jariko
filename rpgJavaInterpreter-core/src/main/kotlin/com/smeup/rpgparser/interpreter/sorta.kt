package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.utils.EBCDICComparator
import java.nio.charset.Charset
import java.util.*

/**
 * The charset should be sort of system setting
 * Cp037    EBCDIC US
 * Cp0280   EBCDIC ITALIAN
 * See: https://www.ibm.com/support/knowledgecenter/SSLTBW_2.1.0/com.ibm.zos.v2r1.idad400/ccsids.htm
 */
fun sortA(value: Value, arrayType: ArrayType, charset: Charset) {
    val ascend: Boolean = arrayType.ascend == null || arrayType.ascend == true

    when (value) {
        is ConcreteArrayValue -> {
            // TODO pass the correct charset to the default sorting algorithm
            if (ascend) {
                value.elements.sort()
            } else {
                value.elements.sortByDescending { it }
            }
        }
        is ProjectedArrayValue -> {
            require(value.field.type is ArrayType)
            val strings = value.field.type.element is StringType
            val n = value.arrayLength
            val multiplier = if (value.field.descend) 1 else -1
            // the good old Bubble Sort
            /*for (i in 1..(value.arrayLength - 1)) {
                for (j in 1..(n - i)) {
                    val compared =
                        if (strings) {
                            value.getElement(j).asString().compare(value.getElement(j + 1).asString(), charset, value.field.descend)
                        } else {
                            value.getElement(j).compareTo(value.getElement(j + 1)) * multiplier
                        }
                    if (compared > 0) {
                        // TODO support data structure swap
                        // For an array data structure, the keyed-ds-array operand is a qualified name
                        // consisting of the array to be sorted followed by the subfield to be used as
                        // a key for the sort.
                        // Swap
                        val tmp = value.getElement(j + 1)
                        value.setElement(j + 1, value.getElement(j))
                        value.setElement(j, tmp)
                    }
                }
            }*/

            val numOfElements = value.arrayLength
            val totalLengthOfAllElements = value.container.len
            val elementSize = totalLengthOfAllElements / numOfElements

            // Extract from each array element, its 'key' value (the subfield) to order by, then
            // store the key into 'keysToBeOrderedBy'
            val keysToBeOrderedBy = Array(numOfElements) { _ -> "" }
            var startElement = 0
            var endElement = elementSize
            (0 until numOfElements).forEach { i ->
                value.container.value.substring(startElement, endElement).apply { keysToBeOrderedBy[i] = this }
                startElement += elementSize
                endElement += elementSize
            }

            // Create a TreeMap with order direction (ascend/descend) needed to
            // store ordered elements.
            val comparator = EBCDICComparator(value.field.descend)
            val orderedElements: MutableMap<String, MutableList<String>> = TreeMap(comparator)

            // Array to ordered Treemap
            keysToBeOrderedBy.indices.forEachIndexed { _, i ->
                with(
                    orderedElements,
                    fun MutableMap<String, MutableList<String>>.() {
                        var add = computeIfAbsent(
                            keysToBeOrderedBy[i].substring(
                                value.field.calculatedStartOffset!!.toInt(),
                                value.field.calculatedEndOffset!!.toInt()
                            )
                        ) { ArrayList() }.add(keysToBeOrderedBy[i])
                    }
                )
            }

            // Set container value with reordered elements
            var containerValue = ""
            orderedElements.forEach { (_: String?, v: List<String>) ->
                v.forEach { s ->
                    containerValue += s
                }
            }
            value.container.value = containerValue
        }
    }
}