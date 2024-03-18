     D A45_A10         C                   '0123456789'
     D A45_A04         S              4    INZ('    ')
     D A45_N10         S              1  0
     D £DBG_Str        S            150          VARYING

     C                   EVAL      A45_A04='2019'
     C     A45_A10       CHECK     A45_A04       A45_N10
     C                   EVAL      £DBG_Str='NUM('+%CHAR(A45_N10)+')'
     C     £DBG_Str      DSPLY