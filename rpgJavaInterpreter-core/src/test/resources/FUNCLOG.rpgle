     DCALL1            PR
     Dp                               2  0
     Dq                               2  0
     Dr                               2  0
      *
     C                   Z-ADD     11            a                 2 0
     C                   Z-ADD     22            b                 2 0
     C                   Z-ADD     *zeros        c                 2 0
     C                   EVAL      c=CALL1(a:b:c)
     C     c             DSPLY
     C                   SETON                                        LR
      *
     PCALL1            B
     DCALL1            PI
     Dp                               2  0
     Dq                               2  0
     Dr                               2  0
     C                   EVAL      r=p+q
     C                   RETURN    r
     PCALL1            E