     V* ==============================================================
     V* 22/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Truncation of number by using Z-ADD. The source is greater
    O *  than destination. Source and destination are decimal.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, operation weren't possible.
     V* ==============================================================
     D SRC             S              6  3 INZ(123.456)
     D MSG             S             10

     C     SRC           DSPLY
     C                   Z-ADD     SRC           RES               4 2          #123.456 cannot be assigned to RES of type NumberType
     C                   EVAL      MSG=%CHAR(RES)
     C     MSG           DSPLY

     C                   SETON                                          LR