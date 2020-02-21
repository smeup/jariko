     D ARRD            S              2  0 DIM(5) ctdata perrcd(1)
     D ARRS            S              2  0 DIM(4) ctdata perrcd(1)
     D MSG             S             10
     D i               S              2  0
     **-------------------------------------------------------------------
     C                   MOVEA(P)  ARRS          ARRD
     C                   FOR       i = 1 TO 5
     C                   EVAL      MSG = %CHAR(ARRD(I))
     C                   DSPLY                   MSG
     C                   ENDFOR
     **-------------------------------------------------------------------
     C                   SETON                                          LR
**CTDATA ARRD
20
30
40
50
60
**CTDATA ARRS
10
11
12
10
