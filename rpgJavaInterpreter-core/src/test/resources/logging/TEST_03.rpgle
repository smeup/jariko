     DFIELD1           S              8  0
     DCOUNT            S              8  0
     D NBR             S              8  0 INZ(10)
     D RESULT          S              8  0 INZ(0)
     DA                S              8  0 INZ(0)
     DB                S              8  0 INZ(1)
      *
      * Test DO loops
     C                   EVAL      A = 0
     C                   EVAL      B = 1
     C                   EVAL      RESULT = 1
     C                   EVAL      COUNT = 100
     C                   DO        COUNT
     C                   EVAL      RESULT = A + B
     C                   EVAL      A = B
     C                   EVAL      B = RESULT
     C                   ENDDO
      *
     C                   EVAL      A = 0
     C                   EVAL      B = 1
     C                   EVAL      RESULT = 1
     C                   EVAL      COUNT = 100
     C                   DO        COUNT
     C                   EVAL      RESULT = A + B
     C                   EVAL      A = B
     C                   EVAL      B = RESULT
     C                   IF        RESULT > 1000
     C                   LEAVE
     C                   ELSE
     C                   ITER
     C                   ENDIF
     C                   ENDDO
     C                   SETON                                        LR
      *--------------------------------------------------------------*



















