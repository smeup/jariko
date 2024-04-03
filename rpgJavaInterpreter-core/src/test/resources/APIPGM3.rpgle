      * Test multiple api inclusion
      * PROC is already defined in APIPROCS but does not must cause a conflict
     DPROC             PR
     Dp                               2  0
     Dq                               2  0
     Dr                               2  0
      *
       /API APIPROCS
      *
     C                   Z-ADD     1             a                 2 0
     C                   Z-ADD     2             b                 2 0
     C                   Z-ADD     *zeros        c                 2 0
     C                   CALLP     PROC(a:b:c)
     C     c             DSPLY
     C                   SETON                                        LR
      *
      *
      *
     PPROC             B
     DPROC             PI
     Dp                               2  0
     Dq                               2  0
     Dr                               2  0
     C                   EVAL      r=p+q
     PPROC             E