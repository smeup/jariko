package com.smeup.dspfparser.positionals

internal const val LAST_COLUMN = 5000
internal const val CONDITION_LENGTH = 10

internal enum class LineInfo(val from: Int, val to: Int) {
    // last has to be one more because .substring is exclusive on endIndex
    SEQUENCE_NUMBER(0, 5),
    A(5, 6),
    COMMENT(6, 80),
    CONDITION(6, 16),
    RESERVED(17, 18),
    TYPE_OF_NAME(16, 17),
    FIELD_NAME(18, 28),
    REFERENCE(28, 29),
    LENGTH(29, 34),
    DTKBS(34, 35),
    DECIMALS_POSITIONS(35, 37),
    FIELD_TYPE(37, 38),
    Y(38, 41),
    X(41, 44),
    KEYWORDS(44, LAST_COLUMN),
}

// kept for reference
private enum class LineInfoOriginal(val from: Int, val to: Int, val startingPosition: Int) {
    SEQUENCE_NUMBER(1, 5, 1),
    A(6, 6, 6),
    COMMENT(7, 80, 7),
    CONDITION(7, 16, 7),
    RESERVED(18, 18, 18),
    TYPE_OF_NAME(17, 17, 17),
    FIELD_NAME(19, 26, 19),
    REFERENCE(29, 29, 29),
    LENGTH(30, 34, 34),
    DTKBS(35, 35, 35),
    DECIMALS_POSITIONS(36, 37, 37),
    FIELD_TYPE(38, 38, 38),
    Y(39, 41, 41),
    X(42, 44, 44),
    KEYWORDS(45, 80, 45),
}
