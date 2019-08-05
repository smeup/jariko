     DFIELD1           S              8  0
     DCOUNT            S              8  0
     D NBR             S              8  0 INZ(10)
     D RESULT          S              8  0 INZ(0)
     DA                S              8  0 INZ(0)
     DB                S              8  0 INZ(1)
      *
     C                   CLEAR                   FIELD1
     C                   IF        FIELD1 > 0
     C                             AND
     C                             RESULT = 0
      * Test: annotation attached to line 14
    MU* VAL1(COUNT) VAL2(0) COMP(EQ)
     C                   EVAL      FIELD1 = 1
      * Test: annotation attached to line 15
    MU* VAL1(COUNT) VAL2(4) COMP(LE)
     C                   EVAL      NBR = 1
     C                   ELSEIF FIELD1 < 0
      * Test: annotation attached to line 21
    MU* VAL1(COUNT) VAL2(4) COMP(LE)
     C                   EVAL      FIELD1 = 1
     C                   ELSE
      * Test: annotation attached to line 25
    MU* VAL1(COUNT) VAL2(4) COMP(LE)
     C                   EVAL      FIELD1 = 12
     C                   ENDIF
     C                   SETON                                        LR
      *--------------------------------------------------------------*



















