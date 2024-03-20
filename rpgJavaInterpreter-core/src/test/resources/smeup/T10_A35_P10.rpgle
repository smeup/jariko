     D £DBG_Str        S            150          VARYING
     D A35_AR1         S             70    DIM(10)
     D A35_A1          S              6
     D A35_A2          S              1
     D A35_AR2         S              2  0 DIM(5)
     D $A              S              1  0
     D I               S              2  0

     C                   SETOFF                                           20
     C                   EVAL      A35_A1='YARRYY'
     C                   EVAL      A35_A2='Y'
     C                   EVAL      $A=3
     C     A35_A2        SCAN      A35_A1:$A     A35_AR2                  20
     C                   EVAL      £DBG_Str=
     C                                  'A35_AR2(01)('+%CHAR(A35_AR2(01))+') '
     C                                 +'A35_AR2(02)('+%CHAR(A35_AR2(02))+') '
     C                                 +'A35_AR2(03)('+%CHAR(A35_AR2(03))+') '
     C                                 +'IN20('+%CHAR(*IN20)+')'

     C     £DBG_Str      DSPLY