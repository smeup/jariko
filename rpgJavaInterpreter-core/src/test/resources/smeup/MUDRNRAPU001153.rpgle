     V* ==============================================================
     V* 17/09/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program increments a variable previously declared
    O *  with *ZEROS.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *     ZeroValue should be a number
     V* ==============================================================
     C                   Z-ADD     *ZEROS        VAR               1 0
     C     VAR           DSPLY
     C                   ADD       1             VAR
     C     VAR           DSPLY

     C                   SETON                                          LR

