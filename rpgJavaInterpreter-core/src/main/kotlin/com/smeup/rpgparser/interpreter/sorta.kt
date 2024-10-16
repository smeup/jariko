package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.utils.EBCDICComparator

/**
 * The charset should be sort of system setting
 * Cp037    EBCDIC US
 * Cp0280   EBCDIC ITALIAN
 * See: https://www.ibm.com/support/knowledgecenter/SSLTBW_2.1.0/com.ibm.zos.v2r1.idad400/ccsids.htm
 */
fun sortA(value: Value, arrayType: ArrayType) {
    val ascend: Boolean = arrayType.ascend == null || arrayType.ascend == true

    when (value) {
        is ConcreteArrayValue -> {
            if (ascend) {
                value.elements.sort()
            } else {
                value.elements.sortByDescending { it }
            }
        }

        is ProjectedArrayValue -> {
            require(value.field.type is ArrayType)

            val numOfElements = value.arrayLength
            val elements: List<String> = value.container.value.chunked(value.step)

            val start = value.startOffset / value.step
            val end = (start + numOfElements).coerceAtMost(elements.size)

            val elementsLeft = if (start > 0) elements.subList(0, start) else emptyList()
            val elementsRight = if (end < elements.size) elements.subList(end, elements.size) else emptyList()

            val elementsToCalculateSort: MutableList<String> = elements.subList(start, end).map {
                it.substring(
                    value.field.calculatedStartOffset!!,
                    value.field.calculatedEndOffset!!
                )
            }.toMutableList()
            // crete a map <Index, ValueToSort>
            val indexesMap: Map<Int, String> = elementsToCalculateSort.mapIndexed { i, v -> i + 1 to v }.toMap()
            // sort the map
            val descend: Boolean = if (value.field.overlayingOn == null) {
                value.field.descend
            } else {
                (value.field.overlayingOn as FieldDefinition).descend
            }
            // sort using EBCDICComparator
            val comparator = EBCDICComparator(descend)
            val sortedValues = indexesMap.values.sortedWith(comparator)
            val sortedMap = indexesMap.entries
                .sortedBy { sortedValues.indexOf(it.value) }
                .associate { it.toPair() }

            val sortedElementsToSort = sortedMap.keys.map { elements[start + it - 1] }

            val resultList = elementsLeft + sortedElementsToSort + elementsRight

            // return value
            value.container.value = resultList.joinToString("")
        }
    }
}