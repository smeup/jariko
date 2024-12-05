     V* ==============================================================
     V* 05/12/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program call a sub program by using pre-initialized
    O *  variables as factors and result.
    O * The called program doesn't make any assignment.
    O * This tests the full behaviour between a CALLER and CALLED, where:
    O * - Called (at the beginning) move Result to Factor 1;
    O * - Caller (at the end) move Result to Factor 1.
     V* ==============================================================
     D PGM_NAME        S             17    INZ('MUDRNRAPU00179_P1')
     D PARM1           S              3    INZ('FOO')
     D RESULT          S              3    INZ('BAZ')

     C                   CALL      PGM_NAME
     C     PARM1         PARM                    RESULT

     C     'CALLER'      DSPLY
     C     PARM1         DSPLY
     C     RESULT        DSPLY

     C                   SETON                                          LR