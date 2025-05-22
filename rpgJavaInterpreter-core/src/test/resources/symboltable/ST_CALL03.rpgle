     V* ==============================================================
     V* 21/05/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Ensures that the param of called program doesn't change type
    O *  into that of caller.
    O * In this case we have DS Array -> S.
     V* ==============================================================
     D DS1             DS
     D  DS1_ARR                       1    DIM(5) INZ('5')

     C     'CALLER'      DSPLY
     C     DS1_ARR(1)    DSPLY
     C     DS1_ARR(2)    DSPLY
     C     DS1_ARR(3)    DSPLY
     C     DS1_ARR(4)    DSPLY
     C     DS1_ARR(5)    DSPLY
     C                   CALL      'ST_CALL03C'
     C                   PARM                    DS1_ARR

     C                   SETON                                        LR