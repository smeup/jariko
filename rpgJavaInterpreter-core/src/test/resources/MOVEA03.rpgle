     D ARRA1           S              4    DIM(3)
     D NEWVAL          S             10    INZ('1234567890')
     D i               S              1  0
     **-------------------------------------------------------------------
     C                   EVAL      ARRA1(1) = 'ABCD'
     C                   EVAL      ARRA1(2) = 'ABCD'
     C                   EVAL      ARRA1(3) = 'ABCD'
     C                   MOVEA     NEWVAL        ARRA1(2)
     C                   FOR       i = 1 TO 3                                   NBR
     C                   DSPLY                   ARRA1(i)
     C                   ENDFOR
     **-------------------------------------------------------------------
     C                   SETON                                          LR
