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
     C                   CLEAR                   A80_AR3
     C                   EVAL      A80_AR3=%SUBARR(A80_AR1:4:2)
     C                   EVAL      £DBG_Str='AR3(1)('+%CHAR(A80_AR3(1))+')'
     C                                    +' AR3(2)('+%CHAR(A80_AR3(2))+')'
     C                                    +' AR3(3)('+%CHAR(A80_AR3(3))+')'
      * Expected 'AR3(1)(13) AR3(2)(3) AR3(3)(0)'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      A80_AR1(1)=9
     C                   EVAL      A80_AR1(2)=5
     C                   EVAL      A80_AR1(3)=16
     C                   EVAL      A80_AR1(4)=13
     C                   EVAL      A80_AR1(5)=3
     C                   CLEAR                   A80_AR2
     C*                   EVAL      %SUBARR(A80_AR2:3:3)=%SUBARR(A80_AR1:2:3)
     C*                   EVAL      £DBG_Str='AR2(1)('+%CHAR(A80_AR2(1))+')'
     C*                                    +' AR2(2)('+%CHAR(A80_AR2(2))+')'
     C*                                    +' AR2(3)('+%CHAR(A80_AR2(3))+')'
     C*                                    +' AR2(4)('+%CHAR(A80_AR2(4))+')'
     C*                                    +' AR2(5)('+%CHAR(A80_AR2(5))+')'
      * Expected 'AR2(1)(0) AR2(2)(0) AR2(3)(5) AR2(4)(16) AR2(5)(13)'
     C*     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR