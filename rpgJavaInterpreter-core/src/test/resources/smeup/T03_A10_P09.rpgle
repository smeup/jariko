     D $IND            S              2  0
     D £DBG_Str        S             110          VARYING

     D* Indicatori come array con indice n *IN(n)
     C                   SETOFF                                           15
     C                   EVAL      $IND=15
     C                   EVAL      £DBG_Str='I('+%CHAR($IND)+')='
     C                               +%CHAR(*IN($IND))
     C                   EVAL      *IN($IND)=*ON
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)
     C                               +' I('+%CHAR($IND)+')='
     C                               +%CHAR(*IN($IND))
     C                   EVAL      *IN($IND)=*OFF
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)
     C                               +' I('+%CHAR($IND)+')='
     C                               +%CHAR(*IN($IND))
     C     £DBG_Str      DSPLY