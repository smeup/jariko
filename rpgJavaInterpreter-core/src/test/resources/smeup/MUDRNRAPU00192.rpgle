     V* ==============================================================
     V* 22/01/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program declares and uses variables and fields as
    O *  Standalone for a simple math operation,
    O *  like `MUDRNRAPU00191` test.
     V* ==============================================================
     D STD1            S              8  0
     D STD2            S              8  0
     D DS1             DS
     D  DS1_F1                        8  0
     D  DS1_F2                        8  0

     D RES             S              8  0
     D MSG             S             25

     C                   EVAL      STD1=20230930
     C                   EVAL      STD2=20230930
     C                   EVAL      RES=STD1+STD2
     C                   EVAL      MSG='STD: ' +
     C                                   %CHAR(RES)
     C     MSG           DSPLY

     C                   EVAL      DS1_F1=20230930
     C                   EVAL      DS1_F2=20230930
     C                   EVAL      RES=DS1_F1+DS1_F2
     C                   EVAL      MSG='DS: ' +
     C                                   %CHAR(RES)
     C     MSG           DSPLY

     C                   EVAL      STD1=12345678
     C                   EVAL      STD2=87654321
     C                   EVAL      RES=STD1+STD2
     C                   EVAL      MSG='STD: ' +
     C                                   %CHAR(RES)
     C     MSG           DSPLY

     C                   EVAL      DS1_F1=12345678
     C                   EVAL      DS1_F2=87654321
     C                   EVAL      RES=DS1_F1+DS1_F2
     C                   EVAL      MSG='DS: ' +
     C                                   %CHAR(RES)
     C     MSG           DSPLY

     C                   SETON                                          LR