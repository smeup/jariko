     D ARR1            S             20    DIM(10)
     D ARR2            S             10    DIM(10) ctdata perrcd(1)
     D i               S              2  0
     **-------------------------------------------------------------------
     C                   EVAL      ARR1=*ALL'X'
     C                   MOVEA     ARR2          ARR1
     C                   FOR       i = 1 TO 10
     C                   DSPLY                   ARR1(i)
     C                   ENDFOR
     **-------------------------------------------------------------------
     C                   SETON                                          LR
**CTDATA ARR2
ABCDEFGHIL
         1
BBBBBBBBBB
BBBBBBBBBB
AAAAAAAAAA
AAAAAAAAAA
  ABCDEFGH
ILMNOPQRST
  ABCDEFGH
ILMNOPQRST
