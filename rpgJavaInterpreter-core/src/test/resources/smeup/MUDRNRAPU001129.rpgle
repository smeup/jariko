     V* ==============================================================
     V* 17/06/2025 APU001 Creation
     V* 20/06/2025 APU001 Editing by adding a final value
     V* 24/06/2025 APU001 Improved program goal
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program calls the procedure `PR2` which calls `PR1`.
    O * Both have the same parameter name and are defined in the main.
    O * So, the parent Symbol Table must be the main (where the
    O *  procedure is defined) and not from the caller.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *   `Issue executing EvalStmt at line 38.
    O *    Cannot find searched value for DataDefinition(name=PR_VAR...`
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
      *
     C                   CLEAR                   PR_TEMPVAR        1 0
     C                   EVAL      PR_TEMPVAR=1+PR1(PR_VAR)
     C                   RETURN    PR_TEMPVAR
      *
     P PR2             E

     P PR1             B
     D PR1             PI             1  0
     D PR_VAR                         1  0
      *
     C                   RETURN    PR_VAR*2
      *
     P PR1             E