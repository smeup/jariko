     D £DBG_Str        S             20           VARYING
      * Indicatori con letter case differente
     C                   EVAL      *in35=*On
      *
     C     *In35         IFEQ      *ON
     C                   EVAL      *inlr=*On
     c                   ENDIF
     C                   EVAL      £DBG_Str='*IN35->'
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)
     C                               +%CHAR(*IN35)
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)
     C                               +', *INLR->'
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)
     C                               +%CHAR(*INLR)
      *
     C     £DBG_Str      DSPLY
