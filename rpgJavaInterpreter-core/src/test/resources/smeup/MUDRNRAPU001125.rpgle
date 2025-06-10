     V* ==============================================================
     V* 09/06/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Using `OCCUR` by passing a variable as argument.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *   `OCCUR not supported. Q£ORAR must be a DS defined
    O *    with OCCURS keyword`
     V* ==============================================================
     D SIZE_OCCURS     C                   CONST(2)
     D DS1             DS                  OCCURS(SIZE_OCCURS) INZ
     D  DS1_FL1                       2  0
     D  DS1_FL2                       2  1
     D TMP             S              5

     C                   Z-ADD     1             I                 4 0
     C     I             OCCUR     DS1
     C                   EXSR      SHOW
     C                   EVAL      DS1_FL1=1
     C                   EVAL      DS1_FL2=1.1
     C                   EXSR      SHOW

     C                   Z-ADD     2             I
     C     I             OCCUR     DS1
     C                   EXSR      SHOW
     C                   EVAL      DS1_FL1=2
     C                   EVAL      DS1_FL2=2.2
     C                   EXSR      SHOW

     C                   SETON                                          LR

     C     SHOW          BEGSR
     C                   EVAL      TMP=%CHAR(DS1_FL1)
     C     TMP           DSPLY
     C                   EVAL      TMP=%CHAR(DS1_FL2)
     C     TMP           DSPLY
     C                   ENDSR

