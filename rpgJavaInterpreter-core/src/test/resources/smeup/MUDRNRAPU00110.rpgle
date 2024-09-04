     V* ==============================================================
     V* 28/08/2024 BMA    Creation
     V* 29/08/2024 APU001 Edit
     V* ==============================================================
    O * PROGRAM GOAL
    O * Assignment, with MOVEL, the content of S (with negative number)
    O *  to two different DS:
    O *  - declared as integer;
    O *  - declared as decimal.
    O * This program doesn't crash.
     V* ==============================================================
     D $NR1            S              5S 0
     D $NR2            S              5S 2
     D DSNR1           DS
     D  NR1                           5  0
     D DSNR2           DS
     D  NR2                           5  2
     D AAA10           S             10

     C                   DO        10            $X                2 0
     C                   EVAL      $NR1=$X*-1                                                           # -1
     C                   MOVEL     $NR1          DSNR1
     C                   EVAL      AAA10=%CHAR(NR1)
     C     AAA10         DSPLY
     C     DSNR1         DSPLY
     C                   ENDDO

     C                   DO        10            $X                2 0
     C                   EVAL      $NR2=$X*-1
     C                   MOVEL     $NR2          DSNR2
     C                   EVAL      AAA10=%CHAR(NR2)
     C     AAA10         DSPLY
     C     DSNR2         DSPLY
     C                   ENDDO