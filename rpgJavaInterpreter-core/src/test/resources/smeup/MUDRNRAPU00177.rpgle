     V* ==============================================================
     V* 05/12/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program call a sub program by using pre-initialized
    O *  variables as factors and result.
    O * The called program doesn't make any assignment.
    O * This tests the full behaviour between a CALLER and CALLED, where:
    O * - Caller (at the beginning) move Factor 2 to Result;
    O * - Called (at the beginning) move Result to Factor 1;
    O * - Called (at the end) move Factor 2 to Result;
    O * - Caller (at the end) move Result to Factor 1.
     V* ==============================================================
     D PGM_NAME        S             17    INZ('MUDRNRAPU00177_P1')
     D PARM1           S              3    INZ('FOO')
     D PARM2           S              3    INZ('BAR')
     D RESULT          S              3    INZ('BAZ')

     C                   CALL      PGM_NAME
     C     PARM1         PARM      PARM2         RESULT

     C     'CALLER'      DSPLY
     C     PARM1         DSPLY
     C     PARM2         DSPLY
     C     RESULT        DSPLY

     C                   SETON                                          LR