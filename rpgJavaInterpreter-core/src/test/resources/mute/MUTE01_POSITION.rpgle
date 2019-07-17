     DFIELD1           S             15A   INZ('AAAA')
     DCOUNT            S              8  0
     D NBR             S              8  0 INZ(10)
     D RESULT          S              8  0 INZ(0)
     DA                S              8  0 INZ(0)
     DB                S              8  0 INZ(1)
      *
     C                   CLEAR                   FIELD1
     C                   FOR       COUNT = 2 TO NBR
      * Test: annotation attached to line 12
    MU* VAL1(DFIELD1) VAL2(4) COMP(LE)
     C                   EVAL      RESULT = A + B
      * Test: annotation attached to line 15
    MU* VAL1(DFIELD1) VAL2(4) COMP(LE)
     C                   EVAL      A = B
      * Test: annotation attached to line 18
    MU* VAL1(COUNT) VAL2(4) COMP(LE)
     C                   EVAL      B = RESULT
     C                   ENDFOR
     C                   SETON                                        LR
      *--------------------------------------------------------------*
