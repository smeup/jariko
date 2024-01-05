     D A80_AR1         S             10I 0 DIM(5)
     D A80_AR2         S             10I 0 DIM(15)
     D A80_AR3         S             10I 0 DIM(20)
     D £DBG_Str        S             52
      *
     C                   EVAL      A80_AR1(1)=9
     C                   EVAL      A80_AR1(2)=5
     C                   EVAL      A80_AR1(3)=16
     C                   EVAL      A80_AR1(4)=13
     C                   EVAL      A80_AR1(5)=3
     C                   CLEAR                   A80_AR2
     C                   CLEAR                   A80_AR3
     C                   EVAL      A80_AR2=%SUBARR(A80_AR1 : 2)
     C                   EVAL      A80_AR3=A80_AR2 + %SUBARR(A80_AR1:2:3)
     C                   EVAL      £DBG_Str =
     C                                  'A80_AR3(1)('+%CHAR(A80_AR3(1))+')'
     C                                 +'A80_AR3(2)('+%CHAR(A80_AR3(2))+')'
     C                                 +'A80_AR3(3)('+%CHAR(A80_AR3(3))+')'
      * Expected 'A80_AR3(1)(10)A80_AR3(2)(32)A80_AR3(3)(26)'
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                          LR