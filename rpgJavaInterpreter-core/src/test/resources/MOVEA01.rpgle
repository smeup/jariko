     D ARRA1           S              4    DIM(3)
     D ARRA2           S              2    DIM(3)
     D XXSTR           S              2
     D YYSTR           S              2
     D ZZSTR           S              4
     D i               S              1  0
     **-------------------------------------------------------------------
     C                   EVAL      XXSTR='AB'
     C                   EVAL      YYSTR='CD'
     C                   EVAL      ZZSTR=XXSTR+YYSTR
     C                   EVAL      ARRA1=ZZSTR
     C                   MOVEA     YYSTR         ARRA1
     C                   FOR       i = 1 TO 3                                   NBR
     C                   DSPLY                   ARRA1(i)
     C                   ENDFOR
     **-------------------------------------------------------------------
     C                   SETON                                          LR
