      *---------------------------------------------------------------
      * A very simple 'SUM' procedure called by 'EVAL' statement
      *---------------------------------------------------------------
     DCALL1            PR             2  0
     Dp                               2  0
     Dq                               2  0
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
     C                   Z-ADD     11            a                 2 0
     C                   Z-ADD     22            b                 2 0
     C                   Z-ADD     *zeros        c                 2 0
     C                   EVAL      c=CALL1(a:b)
     C     c             DSPLY
     C                   SETON                                        LR
      *---------------------------------------------------------------
     PCALL1            B
     DCALL1            PI             2  0
     Dp                               2  0
     Dq                               2  0
     Dr                S              2  0 INZ(*ZEROS)
     C                   EVAL      r=p+q
     C                   RETURN    r
     PCALL1            E