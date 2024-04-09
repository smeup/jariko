     D £DBG_Str        S            150          VARYING

     C                   SETOFF                                       1011
     C                   SETON                                        12
     C                   SELECT
     C                   WHEN      *IN12
     C                   EVAL      £DBG_Str='PrimoWhen'
     C                   WHEN      (*IN10 AND *IN11)
     C                             OR *IN12
     C                   EVAL      £DBG_Str='SecondoWhen'
     C                   OTHER
     C                   EVAL      £DBG_Str='Other'
     C                   ENDSL
     C     £DBG_Str      DSPLY