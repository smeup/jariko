     V* ==============================================================
     V* 23/06/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program calls the procedure `PR2` which calls `PR1`;
    O *  this one calls `PR0`. All procedures are defined in main.
    O * In this case a variable of main is defined into the first
    O *  two called procedures.
     V* ==============================================================
     D MAIN_RESULT     S              2  0
     D MAIN_VAL        S              2  0
     D MAIN_CONST      C                   CONST(5)
      * Procedure prototype
     D
     D PR0             PR             2  0
     D                                2  0
     D PR1             PR             2  0
     D                                2  0
     D PR2             PR             2  0
     D                                2  0
      *
     C                   EVAL      MAIN_VAL=2
     C                   EVAL      MAIN_RESULT=PR2(MAIN_VAL)
     C     MAIN_RESULT   DSPLY
     C                   SETON                                          LR


      * Procedure implementation
     P PR2             B
     D PR2             PI             2  0
     D PR_VAR                         2  0
     D MAIN_VAL        S              2  0
      *
     C                   EVAL      MAIN_VAL=4
     C                   RETURN    1+PR1(PR_VAR)
      *
     P PR2             E

     P PR1             B
     D PR1             PI             2  0
     D PR_VAR                         2  0
     D MAIN_VAL        S              2  0
      *
     C                   EVAL      MAIN_VAL=6
     C                   RETURN    PR0(PR_VAR)*MAIN_VAL
      *
     P PR1             E

     P PR0             B
     D PR0             PI             2  0
     D PR_VAR                         2  0
      *
     C                   RETURN    (PR_VAR*MAIN_CONST)+MAIN_VAL
      *
     P PR0             E