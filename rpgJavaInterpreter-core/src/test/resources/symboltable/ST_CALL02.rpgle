     V* ==============================================================
     V* 21/05/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Ensures that the param of called program doesn't change type
    O *  into that of caller.
    O * In this case we have S -> S with different sizes.
     V* ==============================================================
     D VARSTD          S              6

     C     'CALLER'      DSPLY
     C                   EVAL      VARSTD='FOOBAR'
     C     VARSTD        DSPLY
     C                   CALL      'ST_CALL02C'
     C                   PARM                    VARSTD

     C                   SETON                                        LR