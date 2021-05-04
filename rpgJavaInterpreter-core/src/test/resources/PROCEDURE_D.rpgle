      *---------------------------------------------------------------
      * Tested features:
      * BASIC 'EVAL' AND 'RETURN' STATEMENT TO CALL PROCEDURE AND
      * GET RETURNED VALUE
      * 1. Two parameters are passed to procedure;
      * 2. Procedure execute sum of two values and return the result;
      * 3. Check result of returned sum
      *---------------------------------------------------------------
     DCALL1            PR             2  0
     Dp                               2  0
     Dq                               2  0
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
     C                   Z-ADD     11            a                 2 0
     C                   Z-ADD     22            b                 2 0
     C                   Z-ADD     0             c                 2 0
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