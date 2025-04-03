     V* ==============================================================
     V* 03/04/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program defines several DS field declared as array
    O *  of Packed, with several sizes and scale, and executes several
    O *  sums between different declarations.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * The evaluation of packed number, of DS array item, is wrong.
    O * This implies a wrong sum.
     V* ==============================================================
     D DS1             DS
     D  DS1_F1                        5P 2 DIM(3) INZ
     D  DS1_F2                        5  2 DIM(3) INZ
     D  DS1_F3                       20P 6 DIM(3) INZ
     D  DS1_F4                       20  6 DIM(3) INZ
     D RES             S             50
     D SUM             S              5P 2

     C                   EVAL      DS1_F1(1)=1.5
     C                   EVAL      DS1_F2(1)=1.5
     C                   EVAL      DS1_F3(1)=1.5
     C                   EVAL      DS1_F4(1)=1.5

     C                   EVAl      SUM=DS1_F1(1)+DS1_F2(1)
     C                   EVAL      RES=%CHAR(SUM)
     C     RES           DSPLY

     C                   EVAl      SUM=DS1_F1(1)+DS1_F2(1)
     C                   EVAL      RES=%CHAR(SUM)
     C     RES           DSPLY

     C                   EVAl      SUM=DS1_F1(1)+DS1_F2(1)
     C                   EVAL      RES=%CHAR(SUM)
     C     RES           DSPLY

     C                   EVAl      SUM=DS1_F3(1)+DS1_F4(1)
     C                   EVAL      RES=%CHAR(SUM)
     C     RES           DSPLY

     C                   EVAl      SUM=DS1_F1(1)+DS1_F3(1)
     C                   EVAL      RES=%CHAR(SUM)
     C     RES           DSPLY

     C                   SETON                                          LR
