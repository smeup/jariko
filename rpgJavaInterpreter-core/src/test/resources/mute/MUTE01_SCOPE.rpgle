     DFIELD1           S             15A   INZ('AAAA')
      * Test: annotation attached to line 4
    MU* VAL1(FIELD1) VAL2('AAAA') COMP(EQ)
     DCOUNT            S              8  0
     D NBR             S              8  0 INZ(10)
     D RESULT          S              8  0 INZ(0)
     DA                S              8  0 INZ(0)
      * Test: annotation attached to line 9
     DB                S              8  0 INZ(1)
    MU* VAL1(NBR) VAL2(10) COMP(EQ)
      *
     C                   CLEAR                   FIELD1
     C                   EVAL      FIELD1 = 'A ' + ' B'
      * Test: both annotations are attached to line 16
    MU* VAL1(FIELD1) VAL2('A ' + ' B') COMP(EQ)
    MU* VAL1(NBR) VAL2(0) COMP(EQ)
     C                   EXSR      TEST

     C                   SETON                                        LR
      *--------------------------------------------------------------*
     C     TEST          BEGSR
      * Test: the annotation is attached to line 23
    MU* VAL1(COUNT) VAL2(4) COMP(LE)
     C                   CLEAR                   FIELD1
     C                   FOR       COUNT = 2 TO NBR
     C                   EVAL      RESULT = A + B
     C                   EVAL      A = B
     C                   EVAL      B = RESULT
     C                   ENDFOR
     C                   ENDSR
      *--------------------------------------------------------------*
