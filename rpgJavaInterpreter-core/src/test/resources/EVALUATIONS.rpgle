     D R               S             10  3
     D V02             S             10  3 INZ(2)
     D MSG             S             52
      *
     C                   EVAL      R = -V02
     C                   EVAL      MSG = %CHAR(R)
     C     MSG           DSPLY
      *
     C                   SETON                                        LR