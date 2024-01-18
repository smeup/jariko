     D £DBG_Pas        S              3
     D £DBG_Str        S            100
      *
     D A50_AR1         S             10    DIM(10)
     D A50_AR2         S                   LIKE(A50_AR1) DIM(40)
     D A50_N3          S              2  0
      *
     C                   EVAL      A50_N3 = %ELEM(A50_AR1)
     C                   EVAL      £DBG_Str='A50_AR1('+%CHAR(A50_N3)+')'
     C                   EVAL      A50_N3 = %ELEM(A50_AR2)
     C                   EVAL      £DBG_Str=%TRIMR(£DBG_Str)
     C                                      +' A50_AR2('+%CHAR(A50_N3)+')'
      * Expected:
      * 'A50_AR1(10) A50_AR2(40)'
      *
     C     £DBG_Str      DSPLY
     C                   SETON                                        LR