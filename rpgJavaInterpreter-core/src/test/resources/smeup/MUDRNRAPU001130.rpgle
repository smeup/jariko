     V* ==============================================================
     V* 23/06/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program calls the procedure `PR2` which calls `PR1`.
    O * This one calls `PR0`. All procedures are defined in main
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
     C                   EVAL      MAIN_VAL=1
     C                   EVAL      MAIN_RESULT=PR2(MAIN_VAL)
     C     MAIN_RESULT   DSPLY
     C                   SETON                                          LR


      * Procedure implementation
     P PR2             B
     D PR2             PI             2  0
     D PR_VAR                         2  0
      *
     C                   CLEAR                   PR_TEMPVAR        2 0
     C                   EVAL      PR_TEMPVAR=1+PR1(PR_VAR)
     C                   RETURN    PR_TEMPVAR
      *
     P PR2             E

     P PR1             B
     D PR1             PI             2  0
     D PR_VAR                         2  0
      *
     C                   RETURN    PR0(PR_VAR)*2
      *
     P PR1             E

     P PR0             B
     D PR0             PI             2  0
     D PR_VAR                         2  0
      *
     C                   RETURN    PR_VAR*MAIN_CONST
      *
     P PR0             E