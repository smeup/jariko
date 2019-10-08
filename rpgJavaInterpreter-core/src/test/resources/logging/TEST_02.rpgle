     DFIELD1           S              8  0
     DCOUNT            S              8  0
     D NBR             S              8  0 INZ(10)
     D RESULT          S              8  0 INZ(0)
     DA                S              8  0 INZ(0)
     DB                S              8  0 INZ(1)
      *
      * Test FOR loops
     C                   EVAL      A = 0
     C                   EVAL      B = 1
     C                   EVAL      RESULT = 1
     C                   FOR       COUNT = 1 TO 5
     C                   EVAL      RESULT = A + B
     C                   EVAL      A = B
     C                   EVAL      B = RESULT
     C                   ENDFOR
      * Test FOR DOWN TO
     C                   EVAL      A = 0
     C                   EVAL      B = 1
     C                   EVAL      RESULT = 1
     C                   FOR       COUNT = 100 DOWNTO 1
     C                   EVAL      RESULT = A + B
     C                   EVAL      A = B
     C                   EVAL      B = RESULT
     C                   ENDFOR
      * Test FOR BY
     C                   EVAL      A = 0
     C                   EVAL      B = 1
     C                   EVAL      RESULT = 1
     C                   FOR       COUNT = 1 BY 2 TO 100
     C                   EVAL      RESULT = A + B
     C                   EVAL      A = B
     C                   EVAL      B = RESULT
     C                   ENDFOR
      * Test FOR BY
     C                   EVAL      A = 0
     C                   EVAL      B = 1
     C                   EVAL      RESULT = 1
     C                   FOR       COUNT = 1 TO 100
     C                   EVAL      RESULT = A + B
     C                   EVAL      A = B
     C                   EVAL      B = RESULT
     C                   IF        RESULT > 1000
     C                   LEAVE
     C                   ELSE
      *C                   ITER
     C                   ENDIF
     C                   ENDFOR
     C                   SETON                                        LR
      *--------------------------------------------------------------*



















