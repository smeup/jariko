     V* ==============================================================
     V* 22/01/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program declares and uses variables and fields as Packed
    O *  for a simple math operation.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * On Jariko the final result about the sum between two DS fields
    O *  are wrong and different.
    O * This is caused from wrong de-codification of Packed.
     V* ==============================================================
     D STD1            S              8P 0
     D STD2            S              8P 0
     D DS1             DS
     D  DS1_F1                        8P 0
     D  DS1_F2                        8P 0

     D RES             S              8P 0
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