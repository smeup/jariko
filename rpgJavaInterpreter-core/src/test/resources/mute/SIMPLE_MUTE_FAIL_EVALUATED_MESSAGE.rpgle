     DA                S              8  0 INZ(5)
     DB                S              8  0 INZ(8)
     D RESULT          S              8  0 INZ(0)
     D MSG             S             15    INZ('Failure message')
      *
    MU* FAIL[MSG]
     C                   EVAL      RESULT = A + B
     C                   SETON                                        LR