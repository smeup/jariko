     D A60_N30A        S              3  0
     D A60_N30B        S              3  0
     D £DBG_Str        S             100          VARYING

     D* Definizione numeri negativi (A60_N30A)
     C                   EVAL      A60_N30A=10
     C                   EVAL      A60_N30B=-A60_N30A
     C                   EVAL      £DBG_Str='Res(-A)='+%CHAR(A60_N30B)
     C                   EVAL      A60_N30B= -A60_N30A
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)
     C                                 +' Res( -A)= '+%CHAR(A60_N30B)
     C        £DBG_Str   DSPLY