     D R1              S             15  1                                       Result 1 dec.
     D R2              S             15  2                                       Result 2 dec.
     D R3              S             15  3                                       Result 3 dec.
     D R4              S             15  4                                       Result 4 dec.
     D R5              S             15  5                                       Result 5 dec.
     D R6              S             15  6                                       Result 6 dec.
     D R7              S             15  7                                       Result 7 dec.
     D R8              S             15  8                                       Result 8 dec.
     D RString         S             50    VARYING                               Result string
      *
    MU* VAL1(R1) VAL2(1.200) COMP(EQ)
     C                   EVAL      R1 = 1.200 / 1
    MU* VAL1(RString) VAL2('1.2') COMP(EQ)
     C                   EVAL      RString = %CHAR(R1)
      *
    MU* VAL1(R1) VAL2(3.400) COMP(EQ)
     C                   EVAL      R1 = 6.800 / 2
    MU* VAL1(RString) VAL2('3.4') COMP(EQ)
     C                   EVAL      RString = %TRIM(%CHAR(R1))
      *
    MU* VAL1(R2) VAL2(6.46) COMP(EQ)
     C                   EVAL      R2 = 3.234 * 2
    MU* VAL1(RString) VAL2('6.46') COMP(EQ)
     C                   EVAL      RString = %CHAR(R2)
      *
    MU* VAL1(R3) VAL2(5.357) COMP(EQ)
     C                   EVAL      R3 = 3.234 + 2.123
    MU* VAL1(RString) VAL2('5.357') COMP(EQ)
     C                   EVAL      RString = %CHAR(R3)
      *
    MU* VAL1(R4) VAL2(0.2000) COMP(EQ)
     C                   EVAL      R4 = 0.2000 / 1
    MU* VAL1(RString) VAL2('.2000') COMP(EQ)
     C                   EVAL      RString = %CHAR(R4)
      *
    MU* VAL1(R4) VAL2(2.1110) COMP(EQ)
     C                   EVAL      R4 = 4.2220 / 2
    MU* VAL1(RString) VAL2('2.1110') COMP(EQ)
     C                   EVAL      RString = %CHAR(R4)
      *
    MU* VAL1(R4) VAL2(2.1100) COMP(EQ)
     C                   EVAL      R4 = 4.22 / 2
    MU* VAL1(RString) VAL2('2.1100') COMP(EQ)
     C                   EVAL      RString = %CHAR(R4)
      *
    MU* VAL1(R5) VAL2(3.40909) COMP(EQ)
     C                   EVAL      R5 = 7.500 / 2.2
    MU* VAL1(RString) VAL2('3.40909') COMP(EQ)
     C                   EVAL      RString = %CHAR(R5)
      *
    MU* VAL1(R6) VAL2(33.333333) COMP(EQ)
     C                   EVAL      R6 = 100.000 / 3
    MU* VAL1(RString) VAL2('33.333333') COMP(EQ)
     C                   EVAL      RString = %CHAR(R6)
      *
    MU* VAL1(R7) VAL2(1.2833717) COMP(EQ)
     C                   EVAL      R7 = (10 * -2.3) / (- 1 * - 3.4) /
     C                                 ((0.4 - 1) * - 3) / (- 2 * 5.2) -
     C                                 (9 * -0.1 * 9.3) / (-5.1 *
     C                                 -0.89 * 2)
    MU* VAL1(RString) VAL2('1.2833717') COMP(EQ)
     C                   EVAL      RString = %CHAR(R7)
      *
    MU* VAL1(R8) VAL2(-0284229.19933963)  COMP(EQ)
     C                   EVAL      R8 = (3.181 * -2.3) / (-0.5 * -3.4) /
     C                                 ((0.4 - 1) * -3.999) / (-2 * 5.2) -
     C                                 (9.001 * -0.001) / (-4.123 * 9.5) +
     C                                 (31234 * 9.1) / (1.0000001 * -1)
    MU* VAL1(RString) VAL2('-284229.19933963') COMP(EQ)
     C                   EVAL      RString = %CHAR(R8)
      *
     C                   SETON                                        LR