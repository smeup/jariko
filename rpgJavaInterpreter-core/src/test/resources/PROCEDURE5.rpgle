     DCALL1            PR
     Dp                               2  0
     Dq                               2  0
     Dr                               2  0
      *
     C                   Z-ADD     11            a                 2 0
     C                   Z-ADD     22            b                 2 0
     C                   Z-ADD     *zeros        c                 2 0
     C                   CALLP     CALL1(a:b:c)
     C     c             DSPLY
     C                   SETON                                        LR
      *
     PCALL1            B
     DCALL1            PI
     Dc                               2  0
     Db                               2  0
     Da                               2  0
     C                   EVAL      a=c+b
     PCALL1            E
