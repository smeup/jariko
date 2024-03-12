     D £DBG_Str        S             10          VARYING
      * Indicatori con letter case differente
     C                   EVAL      *inlr=*On
      *
     C                   EVAL      £DBG_Str='*INLR->'
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)
     C                               +%CHAR(*INLR)
      *
     C     £DBG_Str      DSPLY