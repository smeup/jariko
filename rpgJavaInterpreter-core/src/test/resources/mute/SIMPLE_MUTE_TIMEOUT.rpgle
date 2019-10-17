    MU* TIMEOUT(123)
     DA                S              8  0 INZ(5)
     DB                S              8  0 INZ(8)
     D RESULT          S              8  0 INZ(0)
      *
    MU* VAL1(A) VAL2(5) COMP(EQ)
    MU* VAL1(B) VAL2(8) COMP(EQ)
    MU* VAL1(RESULT) VAL2(13) COMP(EQ)
     C                   EVAL      RESULT = A + B
    MU* TIMEOUT(456)
     C                   SETON                                        LR