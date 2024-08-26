     V* ==============================================================
     D* 26/08/24
     D* Purpose: Must fire the following errors
     D* line 25 - Cannot assign tandard number as Packed.
     D* line 37 - Cannot assign tandard number as Packed.
     V* ==============================================================
     D A40_A50         S             50
     D A40_DS1         DS
     D  A40_DS1_F1             1     20
     D  A40_DS1_F2            21     40
     D  A40_DS1_F3            41     45  0
     D  A40_DS1_F4            46     50P 2

     D A40_DS2         DS
     D  A40_DS2_F1                   20
     D  A40_DS2_F2                   20
     D  A40_DS2_F3                    5  0
     D  A40_DS2_F4                    5P 2

     D A40_DS2_F4_S    S             20

     C                   EVAL      A40_A50 = 'Lorem ipsum dolor si'
     C                                         + 't amet, consectetuer'
     C                                         + '0000500520'

     C                   MOVEL     A40_A50       A40_DS1
     C     A40_DS1_F1    DSPLY
     C     A40_DS1_F2    DSPLY
     C     A40_DS1_F3    DSPLY
     C                   EVAL      A40_DS2_F4_S=%CHAR(A40_DS1_F4)
     C     A40_DS2_F4_S  DSPLY

     C                   MOVEL     A40_A50       A40_DS2
     C     A40_DS2_F1    DSPLY
     C     A40_DS2_F2    DSPLY
     C     A40_DS2_F3    DSPLY
     C                   EVAL      A40_DS2_F4_S=%CHAR(A40_DS2_F4)
     C     A40_DS2_F4_S  DSPLY

     C                   SETON                                          LR
