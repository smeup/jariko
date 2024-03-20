     D £DBG_Str        S            150          VARYING

     D A45_A10         C                   '0123456789'
     D A45_A04         S              4    INZ('    ')
     D A45_N10         S              1  0

     C                   SETOFF                                       40
     C                   EVAL      A45_A04='201A'
     C     A45_A10       CHECK     A45_A04                                40
     C                   EVAL      £DBG_Str='IND('+%CHAR(*IN40)+')'

     C     £DBG_Str      DSPLY