     D $IND            S              2  0
     D £DBG_Str        S             110          VARYING

     D* Indicatori come array con indice n *IN(n)
     C                   EVAL      £DBG_Str='*IN(n)->'
     C                   EVAL      $IND=1
     C                   DO        99
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)
     C                               +%CHAR(*IN($IND))
     C                   ENDDO
     C     £DBG_Str      DSPLY
