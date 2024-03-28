     D £DBG_Str        S            150          VARYING
     D A45_A04         S              4    INZ('    ')
     D A45_A10         C                   '0123456789'

     C                   EVAL      A45_A04='201A'
     C     A45_A10       CHECK     A45_A04       A45_N20           1 0
     C                   EVAL      £DBG_Str='NUM('+%CHAR(A45_N20)+')'
     C     £DBG_Str      DSPLY