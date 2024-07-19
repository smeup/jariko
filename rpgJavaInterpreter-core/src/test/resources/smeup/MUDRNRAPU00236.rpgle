     D £DBG_Str        S             2

     DXIR1IN           S              1N   DIM(100)
     D $X              S              5  0

     C                   EVAL      $X=2
     C                   IF        XIR1IN($X)
     C                   ENDIF

     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY