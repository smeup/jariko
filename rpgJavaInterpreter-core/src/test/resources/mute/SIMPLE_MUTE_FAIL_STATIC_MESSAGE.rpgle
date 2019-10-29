     DA                S              8  0 INZ(5)
     DB                S              8  0 INZ(8)
     D RESULT          S              8  0 INZ(0)
      *
    MU* FAIL('This code should not be executed')
     C                   EVAL      RESULT = A + B
     C                   SETON                                        LR