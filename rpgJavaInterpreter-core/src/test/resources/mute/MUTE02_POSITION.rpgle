     DFIELD1           S             15A   INZ('AAAA')
     DCOUNT            S              8  0
     D NBR             S              8  0 INZ(10)
     D RESULT          S              8  0 INZ(0)
     DA                S              8  0 INZ(0)
     DB                S              8  0 INZ(1)
      *
     C                   CLEAR                   FIELD1
     C                   SELECT
     C                   WHEN      NBR = 0
      * Test: annotation attached to line 13
    MU* VAL1(DFIELD1) VAL2(4) COMP(LE)
     C                   EVAL      RESULT = 0
     C                   WHEN      NBR = 1
      * Test: annotation attached to line 17
    MU* VAL1(DFIELD1) VAL2(4) COMP(LE)
     C                   EVAL      RESULT = 1
     C                   OTHER
     C                   EVAL      RESULT = A + B
      * Test: annotation attached to line 23
    MU* VAL1(DFIELD1) VAL2(4) COMP(LE)
    MU* VAL1(DFIELD1) VAL2(4) COMP(LE)
     C                   EVAL      A = B
     C                   EVAL      B = RESULT
     C                   ENDSL
     C                   SETON                                        LR
      *--------------------------------------------------------------*
