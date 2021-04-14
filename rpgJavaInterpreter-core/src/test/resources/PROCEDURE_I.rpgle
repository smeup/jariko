      *---------------------------------------------------------------
     DCALL1            PR
     Dp                               2  0
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
     C                   Z-ADD     1             a                 2 0
     C                   CALLP     CALL1(a)
     C     a             DSPLY
      *
     C                   Z-ADD     2             a                 2 0
     C                   CALLP     CALL1(a)
     C     a             DSPLY
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
     PCALL1            B
     DCALL1            PI
     Dp                               2  0
      *
     C                   SELECT
     C                   WHEN      p=1
     C                   RETURN
     C                   WHEN      p=2
     C                   EVAL      p=p*2
     C                   ENDSL
      *
     PCALL1            E