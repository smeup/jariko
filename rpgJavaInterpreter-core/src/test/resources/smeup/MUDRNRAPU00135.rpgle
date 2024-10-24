     V* ==============================================================
     V* 24/10/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * MOVEL an integer array to another. The size of first is lower
    O *  than destination.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was
    O *  `Cannot set an array as factor 2 in MOVEL/MOVEL(P) statement'
     V* ==============================================================
     D ARR_1           S              1  0 DIM(3) INZ(1)
     D ARR_2           S              1  0 DIM(5) INZ(2)
     D TMP             S              2
     D COUNT           S              2  0 INZ(1)

     C     COUNT         DOUEQ     4
     C                   EVAL      TMP=%CHAR(ARR_1(COUNT))
     C     TMP           DSPLY
     C                   EVAL      COUNT=COUNT+1
     C                   ENDDO

     C                   MOVEL     ARR_1         ARR_2                          #Cannot set an array as factor 2 in MOVEL/MOVEL(P) statement

     C                   EVAL      COUNT=1
     C     COUNT         DOUEQ     6
     C                   EVAL      TMP=%CHAR(ARR_2(COUNT))
     C     TMP           DSPLY
     C                   EVAL      COUNT=COUNT+1
     C                   ENDDO

     C                   SETON                                          LR