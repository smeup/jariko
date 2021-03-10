      *
     D MSG             S             50
      *
      /API APIMATH
      *
     C                   EVAL      A = 10
     C                   EVAL      B = 10
     C                   EXSR      SUM
     C                   EVAL      MSG=%CHAR(Z)
     C     MSG           DSPLY
     C                   SETON                                        LR