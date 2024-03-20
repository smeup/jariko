     D £DBG_Str        S            150          VARYING
     D A35_AR1         S             70    DIM(10)
     D A35_A1          S              6
     D A35_A2          S              1
     D A35_AR2         S              2  0 DIM(5)
     D $A              S              1  0
     D I               S              2  0

     C                   EVAL      A35_AR1(1)='ABCDEF'
     C                   EVAL      A35_AR1(2)='123&5'
     C                   EVAL      A35_AR1(3)='TEST&'
     C                   EVAL      I = 2
     C     '&'           SCAN      A35_AR1(I):1                           45
     C                   EVAL      £DBG_Str=
     C                                     'A35_AR1(2)('+%TRIMR(A35_AR1(2))+') '
     C                                    +'IN45('+%CHAR(*IN45)+')'

     C     £DBG_Str      DSPLY