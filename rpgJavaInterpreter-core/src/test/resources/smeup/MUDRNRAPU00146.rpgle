     V* ==============================================================
     V* 25/10/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * MOVEL a integer array to decimal. The number of digits
    O *  of first are lower than second.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was
    O *  `Value ConcreteArrayValue cannot be assigned
    O *  to data: DataDefinition'
     V* ==============================================================
     D ARR_1           S              3  0 DIM(3) INZ(123)
     D ARR_2           S              6  3 DIM(5) INZ(9.9)
     D TMP             S              7
     D COUNT           S              2  0 INZ(1)

     C     COUNT         DOUEQ     4
     C                   EVAL      TMP=%CHAR(ARR_1(COUNT))
     C     TMP           DSPLY
     C                   EVAL      COUNT=COUNT+1
     C                   ENDDO

     C                   EVAL      ARR_2=ARR_1

     C                   EVAL      COUNT=1
     C     COUNT         DOUEQ     6
     C                   EVAL      TMP=%CHAR(ARR_2(COUNT))
     C     TMP           DSPLY
     C                   EVAL      COUNT=COUNT+1
     C                   ENDDO

     C                   SETON                                          LR