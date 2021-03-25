      *
     D MSG             S             50
      *
      /API APIMATH
      *
     C                   EVAL      X = 99
     C                   EVAL      Y = 1
     C                   EXSR      SUM
     C                   EVAL      MSG=%CHAR(Z)
     C     MSG           DSPLY
     C                   SETON                                        LR