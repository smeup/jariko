     D £DBG_Str        S            150          VARYING
     D A30_SEC         S             15  5

     C                   EVAL      A30_SEC=219120.38
     C                   EVAL      A30_SEC=%INTH(A30_SEC/60)*60
     C                   EVAL      £DBG_Str='Min('+%CHAR(A30_SEC)+') '
      * Arrotondamento ai centesimi
     C                   EVAL      A30_SEC=219120.38
     C                   EVAL      A30_SEC=%INTH(A30_SEC/36)*36
     C                   EVAL      £DBG_Str=%TRIMR(£DBG_Str)
     C                                     +' Cen('+%CHAR(A30_SEC)+') '

     C     £DBG_Str      DSPLY