     D £DBG_Str        S             2

     D C§IN            S              1N   DIM(40)
     D$C               S              5  0
     C                   EVAL      $C=2
     C                   IF        NOT(C§IN($C))
     C                   EVAL      C§IN($C)=*ON
     C                   ENDIF

     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY