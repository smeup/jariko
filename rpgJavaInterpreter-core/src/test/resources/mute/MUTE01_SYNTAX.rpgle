     DFIELD1           S              5A                                        Tappo
     DFIELD2           S              5A
     DFIELD3           S             15A    INZ('AAAA')
     DNUMBER1          S             15 0   INZ(23)
     DNUMBER2          S             15 0
     DNUMBER3          S             15 0
     DCOUNT            S              8  0
     DA                S              8  0 INZ(0)
     DB                S              8  0 INZ(1)
      *
     C                   CLEAR                   FIELD1
     C                   CLEAR                   FIELD2
     C                   CLEAR                   FIELD3
     C                   EVAL      NUMBER1 =  1
     C                   EVAL      NUMBER2 =  2
      * Syntax Test FIELD VS NUMERIC VALUE
    MU* VAL1(NUMBER1) VAL2(0) COMP(NE)
    MU* VAL1(NUMBER1) VAL2(1) COMP(EQ)
    MU* VAL1(NUMBER1) VAL2(2) COMP(LT)
    MU* VAL1(NUMBER1) VAL2(0) COMP(GT)
    MU* VAL1(NUMBER1) VAL2(1) COMP(LE)
    MU* VAL1(NUMBER1) VAL2(1) COMP(GE)
      * Syntax Test FIELD VS FIELD NUMERIC
    MU* VAL1(NUMBER1) VAL2(NUMBER2) COMP(NE)
    MU* VAL1(NUMBER1) VAL2(NUMBER2) COMP(EQ)
    MU* VAL1(NUMBER1) VAL2(NUMBER2) COMP(LT)
    MU* VAL1(NUMBER1) VAL2(NUMBER2) COMP(GT)
    MU* VAL1(NUMBER1) VAL2(NUMBER2) COMP(LE)
    MU* VAL1(NUMBER1) VAL2(NUMBER2) COMP(GE)
      * Syntax Test FIELD VS EXPRESSION NUMERIC
    MU* VAL1(NUMBER1) VAL2(NUMBER1+1) COMP(NE)
    MU* VAL1(NUMBER1) VAL2(NUMBER1+NUMBER2) COMP(EQ)
    MU* VAL1(NUMBER1) VAL2(NUMBER1+NUMBER2+1) COMP(LT)
    MU* VAL1(NUMBER1) VAL2(NUMBER1*NUMBER2) COMP(GT)
     C                   EVAL      NUMBER2 =  3
     C                   EVAL      NUMBER3 = NUMBER1 + NUMBER2 * 2
     C                   EVAL      NUMBER3 = (NUMBER1 + NUMBER2) * 2
    MU* VAL1(NUMBER2) VAL2(NUMBER1+1-NUMBER3) COMP(LT)
     C                   EVAL      NUMBER3 = (NUMBER1 + NUMBER2) * 2
      *
     C                   SETON                                        LR
      *
      *
      *
