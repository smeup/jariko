     V* ==============================================================
     V* 25/10/2024 APU001 Creation
     V* 29/10/2024 APU001 Improvements
     V* 31/10/2024 APU001 Typo
     V* ==============================================================
    O * PROGRAM GOAL
    O * EVAL a decimal array to integer.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the result of array have latest two
    O *  elements as 0.
     V* ==============================================================
     D ARR_1           S              5  3 DIM(3) INZ(1.2)
     D ARR_2           S              5  0 DIM(5) INZ(2)
     D TMP             S              5
     D COUNT           S              2  0 INZ(1)

     C                   FOR       COUNT=1 TO %ELEM(ARR_1)
     C                   EVAL      TMP=%CHAR(ARR_1(COUNT))
     C     TMP           DSPLY
     C                   ENDFOR

     C                   EVAL      ARR_2=ARR_1

     C                   FOR       COUNT=1 TO %ELEM(ARR_2)
     C                   EVAL      TMP=%CHAR(ARR_2(COUNT))
     C     TMP           DSPLY
     C                   ENDFOR

     C                   SETON                                          LR