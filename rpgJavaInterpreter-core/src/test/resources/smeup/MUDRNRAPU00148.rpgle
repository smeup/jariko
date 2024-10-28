     V* ==============================================================
     V* 25/10/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * WVAL an integer array to another. The size of first is lower
    O *  than destination, and the target is ana array of DS.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the result of array have latest two
    O *  elements as 0.
     V* ==============================================================
     D ARR_1           S              1  0 DIM(3) INZ(1)
     D DS_1            DS
     D DS_ARR_1                       1  0 DIM(5) INZ(2)

     D TMP             S              7
     D COUNT           S              2  0 INZ(1)

     C     COUNT         DOUEQ     4
     C                   EVAL      TMP=%CHAR(ARR_1(COUNT))
     C     TMP           DSPLY
     C                   EVAL      COUNT=COUNT+1
     C                   ENDDO

     C                   EVAL      DS_ARR_1=ARR_1

     C                   EVAL      COUNT=1
     C     COUNT         DOUEQ     6
     C                   EVAL      TMP=%CHAR(DS_ARR_1(COUNT))
     C     TMP           DSPLY
     C                   EVAL      COUNT=COUNT+1
     C                   ENDDO

     C                   SETON                                          LR