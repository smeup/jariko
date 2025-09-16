     V* ==============================================================
     V* 16/09/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program performs `IF` statement between a *ZEROES and
    O *  integer.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *     Cannot compare ZeroValue to IntValue(value=999)
     V* ==============================================================
     C                   IF        *ZEROS<999
     C     'TRUE'        DSPLY
     C                   ENDIF

     C                   IF        999>*ZEROS
     C     'TRUE'        DSPLY
     C                   ENDIF

     C                   SETON                                          LR
