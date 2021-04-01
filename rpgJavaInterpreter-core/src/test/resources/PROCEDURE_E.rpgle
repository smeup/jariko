     DMyProcedure      PR
     Dp                               2  0
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
     C                   Z-ADD     11            a                 2 0
      * Value of 'a' before call procedure (must be 11)
     C*     a             DSPLY
     C                   CALLP     MyProcedure(a)
      * Value of 'a' after call procedure (must be 22)
     C     a             DSPLY
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
     PMyProcedure      B
     DMyProcedure      PI
     Dp                               2  0
      *
      * Value of received parameter 'p' (must be 11)
     C*     p             DSPLY
     C                   EVAL      p=22
      * Value of received parameter 'p' re-assigned (must be 22)
     C*     p             DSPLY
      *
     PMyProcedure      E
      *---------------------------------------------------------------
