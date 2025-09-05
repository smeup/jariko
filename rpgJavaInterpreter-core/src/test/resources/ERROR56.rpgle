     V* ==============================================================
     V* 20/06/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program calls the procedure `PR2` which calls `PR1`.
    O * `PR1` try to use a variable defined from its caller `PR2`;
    O * in this case the program mustn't compile.
     V* ==============================================================
     D MAIN_RESULT     S              1  0
     D MAIN_VAL        S              1  0
      * Procedure prototype
     D
     D PR1             PR             1  0
     D                                1  0
     D PR2             PR             1  0
     D                                1  0
      *
     C                   EVAL      MAIN_VAL=1
     C                   EVAL      MAIN_RESULT=PR2(MAIN_VAL)
     C     MAIN_RESULT   DSPLY
     C                   SETON                                          LR


      * Procedure implementation
     P PR2             B
     D PR2             PI             1  0
     D PR_VAR                         1  0
     D PR2_VAR         S              1  0
      *
     C                   EVAL      PR2_VAR=5
     C                   CLEAR                   PR_TEMPVAR        1 0
     C                   EVAL      PR_TEMPVAR=1+PR1(PR_VAR)
     C                   RETURN    PR_TEMPVAR
      *
     P PR2             E

     P PR1             B
     D PR1             PI             1  0
     D PR_VAR                         1  0
      *
     C     PR2_VAR       DSPLY
     C                   RETURN    PR_VAR*2
      *
     P PR1             E