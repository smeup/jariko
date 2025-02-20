     V* ==============================================================
     V* 14/02/2025 APU001 Creation
     V* 20/02/2025 APU001 Improvement
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program defines an `S` with its name, and its field name,
    O *  like those already declared from file.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *     `S$INTE has been defined twice`
     V* ==============================================================
     FB£G11G0V  IF   E           K DISK

     D S$INTE          S             22                                         # S$INTE has been defined twice

     C                   EVAL      S$INTE='FOO'
     C     S$INTE        DSPLY

     C                   SETON                                        RT