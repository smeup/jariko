     DFIELD1           S              8  0
     DCOUNT            S              8  0
     D NBR             S              8  0 INZ(10)
     D RESULT          S              8  0 INZ(0)
     DA                S              8  0 INZ(0)
     DB                S              8  0 INZ(1)
      *
      * Test DOW loops
     C                   EVAL      A = 0
     C                   EVAL      B = 1
     C                   EVAL      RESULT = 1
     C                   EVAL      COUNT = 1
     C                   DOW       COUNT < 100
     C                   EVAL      RESULT = A + B
     C                   EVAL      A = B
     C                   EVAL      B = RESULT
     C                   EVAL      COUNT = COUNT + 1
     C                   ENDDO
     C                   SETON                                        LR
      *--------------------------------------------------------------*



















