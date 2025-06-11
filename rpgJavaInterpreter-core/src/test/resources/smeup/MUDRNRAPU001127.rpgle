     V* ==============================================================
     V* 11/06/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Using `Z-ADD` with the fields declared by using offsets.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *   `Issue executing ZAddStmt at line 21.
    O *    Asked startOffset=24, endOffset=42 on string of length 41
     V* ==============================================================
     D DS1             DS
     D  DS1_FL1                1     18P 2 DIM(6)
     D  DS1_FL2               19     36P 2 DIM(6)
     D  DS1_FL3               37     39P 2
     D  DS1_FL4               40     41
     D TMP             S              5
     D I               S              1  0

     C                   Z-ADD     1             DS1_FL1
     C                   Z-ADD     2             DS1_FL2
     C                   Z-ADD     3             DS1_FL3
     C                   MOVE      *BLANK        DS1_FL4

     C                   EXSR      SHOW

     C                   SETON                                          LR

     C     SHOW          BEGSR
     C                   FOR       I = 1 TO 6
     C                   EVAL      TMP=%CHAR(DS1_FL1(I))
     C     TMP           DSPLY
     C                   ENDFOR

     C                   FOR       I = 1 TO 6
     C                   EVAL      TMP=%CHAR(DS1_FL2(I))
     C     TMP           DSPLY
     C                   ENDFOR

     C                   EVAL      TMP=%CHAR(DS1_FL3)
     C     TMP           DSPLY

     C                   EVAL      TMP=%CHAR(DS1_FL4)
     C     TMP           DSPLY
     C                   ENDSR