     V* ==============================================================
     V* 26/08/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Assignment, with EVAL, the content of S to two different DS:
    O * - declared with offsets;
    O * - declared with sizes.
    O * Field 3 and 4 are numbers, integer and decimal rispectively.
    O * This program doesn't crash.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was
    O *  `Cannot assign DataStructValue to StringType`.
     V* ==============================================================
     D A40_A50         S             50
     D A40_DS1         DS
     D  A40_DS1_F1             1     20
     D  A40_DS1_F2            21     40
     D  A40_DS1_F3            41     45  0
     D  A40_DS1_F4            46     50S 2

     D A40_DS2         DS
     D  A40_DS2_F1                   20
     D  A40_DS2_F2                   20
     D  A40_DS2_F3                    5  0
     D  A40_DS2_F4                    5S 2

     D A40_DS2_F4_S    S             20

     C                   EVAL      A40_A50 = 'Lorem ipsum dolor si'
     C                                         + 't amet, consectetuer'
     C                                         + '0000500520'

     C                   EVAL      A40_DS1=A40_A50                              Jariko Runtime Error
     C     A40_DS1_F1    DSPLY
     C     A40_DS1_F2    DSPLY
     C     A40_DS1_F3    DSPLY
     C                   EVAL      A40_DS2_F4_S=%CHAR(A40_DS1_F4)
     C     A40_DS2_F4_S  DSPLY

     C                   EVAL      A40_DS2=A40_A50                              Jariko Runtime Error
     C     A40_DS2_F1    DSPLY
     C     A40_DS2_F2    DSPLY
     C     A40_DS2_F3    DSPLY
     C                   EVAL      A40_DS2_F4_S=%CHAR(A40_DS2_F4)
     C     A40_DS2_F4_S  DSPLY