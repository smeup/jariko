     D ARRA1           S              4  0 DIM(2)
     D i               S              1  0
     D Msg             S             20
     **-------------------------------------------------------------------
     C                   EVAL      ARRA1=1234
     C                   FOR       i = 1 TO 2
     C                   eval      Msg = %CHAR(ARRA1(i))
     C     Msg           dsply
     C                   ENDFOR
     C                   MOVEA     56            ARRA1
     C                   FOR       i = 1 TO 2
     C                   eval      Msg = %CHAR(ARRA1(i))
     C     Msg           DSPLY
     C                   ENDFOR
     **-------------------------------------------------------------------
     C                   SETON                                          LR
