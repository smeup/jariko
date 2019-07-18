     DFIELD1           S             15A   INZ('AAAA')
      * Test: annotation at runtime evaluate TRUE
    MU* VAL1(FIELD1) VAL2('AAAA') COMP(EQ)
     DCOUNT            S              8  0
     D NBR             S              8  0 INZ(10)
      * Test: annotation at runtime evaluate TRUE
    MU* VAL1(NBR) VAL2(11) COMP(LT)
     D RESULT          S              8  0 INZ(0)
     DA                S              8  0 INZ(0)
     DB                S              8  0 INZ(1)
      *
     C                   EVAL      FIELD1 = 'A ' + ' B'
      * Test: annotation at runtime evaluate FAILS
    MU* VAL1(FIELD1) VAL2('A ' + ' B') COMP(NE)
      * Test: annotation at runtime evaluate TRUE
    MU* VAL1(B) VAL2(1) COMP(GE)
    MU* VAL1(B) VAL2(1) COMP(LE)
      * Test: annotation at runtime evaluate FALSE
    MU* VAL1(B) VAL2(1) COMP(GT)
    MU* VAL1(B) VAL2(1) COMP(LT)
      * Test: tricky if you remove the CLEAR
     C                   CLEAR                   FIELD1
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
