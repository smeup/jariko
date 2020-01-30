     DCALL1            PR
     Dr                               2  0
      *
     C                   Z-ADD     11            a                 2 0
     C                   Z-ADD     22            b                 2 0
     C                   Z-ADD     *zeros        c                 2 0
     C                   CALLP     CALL1(c)
     C     c             DSPLY
     C                   SETON                                        LR
      *
     PCALL1            B
     DCALL1            PI
     Dr                               2  0
     Da                s              2  0 INZ(3)
     C                   EVAL      r=a+b
     PCALL1            E
