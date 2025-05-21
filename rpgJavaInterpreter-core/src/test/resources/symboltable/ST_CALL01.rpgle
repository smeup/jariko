     V* ==============================================================
     V* 21/05/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Ensures that the param of called program doesn't change type
    O *  into that of caller.
    O * In this case we have DS -> S.
     V* ==============================================================
     D £G90DS          DS           512

     C                   EVAL      £G90DS='CALLER'
     C                   CALL      'ST_CALL01C'
     C                   PARM                    £G90DS

     C     £G90DS        DSPLY
     C                   SETON                                        LR