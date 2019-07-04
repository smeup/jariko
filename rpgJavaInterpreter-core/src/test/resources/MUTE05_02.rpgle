     DFIELD1           S              5A                                        Tappo
     DFIELD2           S              5A
     DFIELD3           S             15A
     DNUMBER1          S             15 0
     DNUMBER2          S             15 0
     DNUMBER3          S             15 0
      *
     C                   CLEAR                   FIELD1
     C                   CLEAR                   FIELD2
     C                   CLEAR                   FIELD3
    MU* VAL1(FIELD1) VAL2('A  B') COMP(EQ)
     C                   EVAL      FIELD1 = 'A ' + ' B'
    MU* VAL1(FIELD2) VAL2('A B') COMP(EQ)
     C                   EVAL      FIELD2 = 'A B'
    MU* VAL1(FIELD3) VAL2('A  B  A B') COMP(EQ)
     C                   EVAL      FIELD3 = FIELD1 + ' ' + FIELD2
      *
     C                   EVAL      FIELD1='A'
    MU* VAL1(FIELD1) VAL2(FIELD2) COMP(EQ)
     C                   EVAL      FIELD2='A'
      *
     C                   CLEAR                   NUMBER1
     C                   CLEAR                   NUMBER2
     C                   CLEAR                   NUMBER3
    MU* VAL1(NUMBER1) VAL2(3) COMP(EQ)
     C                   EVAL      NUMBER1 =  3
    MU* VAL1(NUMBER2) VAL2(5) COMP(EQ)
     C                   EVAL      NUMBER2 =  5
    MU* VAL1(NUMBER3) VAL2(13) COMP(EQ)
     C                   EVAL      NUMBER3 = NUMBER1 + NUMBER2 * 2
    MU* VAL1(NUMBER3) VAL2(16) COMP(EQ)
     C                   EVAL      NUMBER3 = (NUMBER1 + NUMBER2) * 2
      *
     C                   SETON                                        LR
