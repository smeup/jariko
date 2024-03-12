     D £DBG_Str        S            150          VARYING
     D T40_AR2         S             10    DIM(5)
     D A20N            S              2  0

     C                   EVAL      T40_AR2(01)='TEST01'
     C                   EVAL      T40_AR2(02)='TEST02'
     C                   EVAL      T40_AR2(03)='TEST03'
     C                   EVAL      A20N=2
     C                   MOVEA(P)  'NO_TEST'     T40_AR2 (A20N)
     C                   EVAL      £DBG_Str='T40_AR2('+
     C                                    %TRIM(T40_AR2(01))+';'+
     C                                    %TRIM(T40_AR2(02))+';'+
     C                                    %TRIM(T40_AR2(03))+')'

     C     £DBG_Str      DSPLY