     V* ==============================================================
     V* 10/06/2025 APU001 Creation
     V* 11/06/2025 APU001 Improvements
     V* ==============================================================
    O * PROGRAM GOAL
    O * Using `Z-ADD` with a field of occurable DS declared as array.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *   `Issue executing ZAddStmt at line 21.
    O *    OccurableDataStructValue cannot be cast to class
    O *    DataStructValue`
     V* ==============================================================
     D SIZE_OCCURS     C                   CONST(2)
     D DS1             DS                  OCCURS(SIZE_OCCURS) INZ
     D  DS1_FL1                       2  0 DIM(3)
     D TMP             S              5

     C     1             OCCUR     DS1
     C                   EXSR      SHOW
     C                   Z-ADD     1             DS1_FL1
     C                   EXSR      SHOW

     C     2             OCCUR     DS1
     C                   EXSR      SHOW
     C                   Z-ADD     2             DS1_FL1
     C                   EXSR      SHOW

     C     1             OCCUR     DS1
     C                   EXSR      SHOW
     C     2             OCCUR     DS1
     C                   EXSR      SHOW

     C                   SETON                                          LR

     C     SHOW          BEGSR
     C                   EVAL      TMP=%CHAR(DS1_FL1(1))
     C     TMP           DSPLY
     C                   EVAL      TMP=%CHAR(DS1_FL1(2))
     C     TMP           DSPLY
     C                   EVAL      TMP=%CHAR(DS1_FL1(3))
     C     TMP           DSPLY
     C                   ENDSR