     DCALL1            PR
     Dp                               2  0 const
     Dq                               2  0
     Dr                               2  0
      *
     C                   Z-ADD     22            b                 2 0
     C                   Z-ADD     *zeros        c                 2 0
     C                   CALLP     CALL1(11.4:b:c)
     C     c             DSPLY
     C                   SETON                                        LR
      *
     PCALL1            B
     DCALL1            PI
     Dp                               2  0 const
     Dq                               2  0
     Dr                               2  0
     C                   EVAL      r=p+q
     PCALL1            E
