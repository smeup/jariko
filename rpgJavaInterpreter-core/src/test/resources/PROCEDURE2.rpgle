 DCALL1            PR             2  0
     Dp                               2  0
     Dq                               2  0
      *
     C                   Z-ADD     11            a                 2 0
     C                   Z-ADD     22            b                 2 0
     C                   Z-ADD     *zeros        c                 2 0
     C                   EVAL      c=CALL1(A:B)
     C     c             DSPLY
     C                   SETON                                        LR
      *
     PCALL1            B
     DCALL1            PI             2  0
     Dp                               2  0
     Dq                               2  0
     C                   RETURN    p+q
     PCALL1            E
