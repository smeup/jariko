     D £DBG_Str        S             50    VARYING

     C                   MOVEL     *ON           *IN01
     C                   MOVEL     *OFF          *INKA
      *
     C                   EVAL      £DBG_Str= '*INKA('+%CHAR(*INKA)+')'
     C                                     +' *IN01('+%CHAR(*IN01)+')'
     C     £DBG_Str      DSPLY
      * result: '*INKA(0) *IN01(1)'
      *
     C                   MOVEL     *OFF          *IN01
     C                   MOVEL     *ON           *INKA
      *
     C                   EVAL      £DBG_Str= '*INKA('+%CHAR(*INKA)+')'
     C                                     +' *IN01('+%CHAR(*IN01)+')'
     C     £DBG_Str      DSPLY
      * result: '*INKA(1) *IN01(0)'
     C                   SETON                                        LR
