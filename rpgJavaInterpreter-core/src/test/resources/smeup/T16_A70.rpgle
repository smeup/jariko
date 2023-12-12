     D A70_AR1         S             20    DIM(10)
     D A70_AR2         S             10    DIM(20)
     D A70_DS1         DS            20    OCCURS(30)
      *
     D A70_N50         S              5P 0
     D £DBG_Str        S             52
      *
     C                   EVAL      A70_N50 = %ELEM(A70_AR1)
     C                   EVAL      £DBG_Str ='A70_AR1('+%CHAR(A70_N50)+')'
     C                   EVAL      A70_N50 = %ELEM (A70_AR2)
     C                   EVAL      £DBG_Str =%TRIMR(£DBG_Str)
     C                                      +' A70_AR2('+%CHAR(A70_N50)+')'
     C                   EVAL      A70_N50 = %ELEM (A70_DS1)
     C                   EVAL      £DBG_Str =%TRIMR(£DBG_Str)
     C                                      +' A70_DS1('+%CHAR(A70_N50)+')'
      * Expect 'A70_AR1(10) A70_AR2(20) A70_DS1(30)'
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR