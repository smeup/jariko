     V* ==============================================================
     D* 05/11/24
     D* Purpose: Must fire the following errors during execution of
     D* C                   MOVEA(P)  DS1_ARR1      ARR1
     D* line 18 - "Factor 2 and Result with different type and size."
     V* ==============================================================
     D DS1             DS           100
     D  DS1_ARR1                      5  3 DIM(3) INZ(12.345)
     D ARR1            S              3  0 DIM(5) INZ(9)
     D TMP             S              7
     D COUNT           S              2  0 INZ(1)

     C                   FOR       COUNT=1 TO %ELEM(ARR1)
     C                   EVAL      TMP=%CHAR(ARR1(COUNT))
     C     TMP           DSPLY
     C                   ENDFOR

     C                   MOVEA(P)  DS1_ARR1      ARR1

     C                   FOR       COUNT=1 TO %ELEM(ARR1)
     C                   EVAL      TMP=%CHAR(ARR1(COUNT))
     C     TMP           DSPLY
     C                   ENDFOR

     C                   SETON                                          LR