     V* ==============================================================
     V* 03/04/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program defines several DS field declared as array
    O *  of Packed, with several sizes and scale.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * The evaluation of packed number, of DS array item, is wrong.
     V* ==============================================================
     D DS1             DS
     D  DS1_F1                        5P 2 DIM(3) INZ
     D  DS1_F2                        5  2 DIM(3) INZ
     D  DS1_F3                       20P 6 DIM(3) INZ
     D  DS1_F4                       20  6 DIM(3) INZ
     D  DS1_F5                        5P 0 DIM(3) INZ
     D  DS1_F6                        5  0 DIM(3) INZ
     D  DS1_F7                       20P 0 DIM(3) INZ
     D  DS1_F8                       20  0 DIM(3) INZ
     D RES             S             50
     D SUM             S              5P 2

     C                   EVAL      DS1_F1(1)=1.5
     C                   EVAL      RES=%CHAR(DS1_F1(1))
     C     RES           DSPLY

     C                   EVAL      DS1_F2(1)=1.5
     C                   EVAL      RES=%CHAR(DS1_F2(1))
     C     RES           DSPLY

     C                   EVAL      DS1_F3(1)=1.5
     C                   EVAL      RES=%CHAR(DS1_F3(1))
     C     RES           DSPLY

     C                   EVAL      DS1_F4(1)=1.5
     C                   EVAL      RES=%CHAR(DS1_F4(1))
     C     RES           DSPLY

     C                   EVAL      DS1_F5(1)=1
     C                   EVAL      RES=%CHAR(DS1_F5(1))
     C     RES           DSPLY

     C                   EVAL      DS1_F6(1)=1
     C                   EVAL      RES=%CHAR(DS1_F6(1))
     C     RES           DSPLY

     C                   EVAL      DS1_F7(1)=1
     C                   EVAL      RES=%CHAR(DS1_F7(1))
     C     RES           DSPLY

     C                   EVAL      DS1_F8(1)=1
     C                   EVAL      RES=%CHAR(DS1_F8(1))
     C     RES           DSPLY

     C                   SETON                                          LR
