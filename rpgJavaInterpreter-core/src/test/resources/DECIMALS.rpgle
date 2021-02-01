     D R               S             10  3
     D MSG             S             52
      *
      * Expected 6.468
     C                   EVAL      R = 3.234 * 2
     C                   EVAL      MSG = %CHAR(R)
     C     MSG           DSPLY
      *
      * Expected 6.700
     C                   EVAL      R = 3.350 * 2
     C                   EVAL      MSG = %CHAR(R)
     C     MSG           DSPLY
      *
      * Expected 5.357
     C                   EVAL      R = 3.234 + 2.123
     C                   EVAL      MSG = %CHAR(R)
     C     MSG           DSPLY
      *
      * Expected 3.400
     C                   EVAL      R = 3.299 + 0.101
     C                   EVAL      MSG = %CHAR(R)
     C     MSG           DSPLY
      *
      * Expected 1.111
     C                   EVAL      R = 3.234 - 2.123
     C                   EVAL      MSG = %CHAR(R)
     C     MSG           DSPLY
      *
      * Expected 3.400
     C                   EVAL      R = 3.402 - 0.002
     C                   EVAL      MSG = %CHAR(R)
     C     MSG           DSPLY
      *
      * Expected 2.111
     C                   EVAL      R = 4.222 / 2
     C                   EVAL      MSG = %CHAR(R)
     C     MSG           DSPLY
      *
      * Expected 3.410
     C                   EVAL      R = 6.820 / 2
     C                   EVAL      MSG = %CHAR(R)
     C     MSG           DSPLY
      *
      * Expected 3.400
     C                   EVAL      R = 6.800 / 2
     C                   EVAL      MSG = %CHAR(R)
     C     MSG           DSPLY
      *
      * Expected 3.500
     C                   EVAL      R = 7.000 / 2
     C                   EVAL      MSG = %CHAR(R)
     C     MSG           DSPLY
      *
      * Expected 3.600
     C                   EVAL      R = 7.200 / 2
     C                   EVAL      MSG = %CHAR(R)
     C     MSG           DSPLY
      *
     C                   SETON                                        LR