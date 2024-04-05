      * Test multiple api inclusion
      * PROCA is already defined in APIPROCS but does not must cause a conflict
     DPROCA            PR
     Dp                               2  0
     Dq                               2  0
     Dr                               2  0

     DPROCB            PR
     Dp                               2  0
     Dq                               2  0
     Dr                               2  0
      *
       /API APIPROCS
      *
     C                   Z-ADD     1             a                 2 0
     C                   Z-ADD     2             b                 2 0
     C                   Z-ADD     *zeros        c                 2 0
     C                   CALLP     PROCA(a:b:c)
     C     c             DSPLY

     C                   CALLP     PROCB(a:b:c)
     C     c             DSPLY

     C                   SETON                                        LR
      *
      *
      *
     PPROCA            B
     DPROCA            PI
     Dp                               2  0
     Dq                               2  0
     Dr                               2  0
     C                   EVAL      r=p+q
     PPROCA            E