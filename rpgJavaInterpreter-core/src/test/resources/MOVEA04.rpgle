     D ARRA1           S              4    DIM(3)
     D NEWVAL          S              3    DIM(2)
     D i               S              1  0
     **-------------------------------------------------------------------
     C                   EVAL      ARRA1(1) = 'ABCD'
     C                   EVAL      ARRA1(2) = 'ABCD'
     C                   EVAL      ARRA1(3) = 'ABCD'
     C                   EVAL      NEWVAL(1) = '123'
     C                   EVAL      NEWVAL(2) = '456'
     C                   MOVEA     NEWVAL        ARRA1(2)
     C                   FOR       i = 1 TO 3
     C                   DSPLY                   ARRA1(i)
     C                   ENDFOR
     **-------------------------------------------------------------------
     C                   SETON                                          LR
