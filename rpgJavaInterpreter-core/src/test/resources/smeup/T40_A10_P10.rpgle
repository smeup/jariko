     D £DBG_Str        S            150          VARYING
     C                   EVAL      A10_D1='AAA'
     C                   EVAL      £DBG_Str='Contenuto Pre-RESET: '+A10_D1
     C                   RESET                   A10_D1           10
     C                   EVAL      £DBG_Str=%TRIMR(£DBG_Str)
     C                                     +' - Contenuto Post-RESET: '+A10_D1
     C     £DBG_Str      DSPLY
