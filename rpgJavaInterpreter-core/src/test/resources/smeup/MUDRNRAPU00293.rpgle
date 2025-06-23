     V* ==============================================================
     V* 19/05/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Incrementing a packed field in a DS
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, Jariko didn't update the value
     V* ==============================================================
     D £139DSO         DS            20
     D  £139O_NK                      4P 0
     C                   EVAL      £139O_NK=0
     C                   EVAL      £139O_NK=£139O_NK+1
     C     £139O_NK      DSPLY
     C                   SETON                                          LR
