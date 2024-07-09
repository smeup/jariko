package com.smeup.dspfparser.linesclassifier

/**
 * All possible values of a field type (what it can be used for).
 */
enum class DSPFFieldType {
    OUTPUT,
    INPUT,
    INPUT_OUTPUT,
    HIDDEN,
    MESSAGE,
    PROGRAM_TO_SYSTEM,
    DEFAULT,
}
