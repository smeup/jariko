     D A20_AR2         S              2  0 DIM(6)
     D A20_AR3         S              2  0 DIM(6)
     D A20_N60         S              6  0
     D A20_I           S              2  0
     D £DBG_Str        S             100          VARYING

     D* DIV con elementi di array
     C                   EVAL      A20_N60=20
     C                   EVAL      A20_I=3
     C                   EVAL      A20_AR2(1)=20
     C                   EVAL      A20_AR2(2)=22
     C                   EVAL      A20_AR2(3)=24
     C                   EVAL      A20_AR2(4)=26
     C                   EVAL      A20_AR2(5)=28
     C                   EVAL      A20_AR2(6)=30
     C                   EVAL      A20_AR3(1)=2
     C                   EVAL      A20_AR3(2)=2
     C                   EVAL      A20_AR3(3)=2
     C                   EVAL      A20_AR3(4)=2
     C                   EVAL      A20_AR3(5)=2
     C                   EVAL      A20_AR3(6)=2
     C     A20_AR2(A20_I)DIV       A20_AR3(A20_I)A20_AR2(A20_I)
     C                   EVAL      £DBG_Str='Res('
     C                                    +%CHAR(A20_AR2(1))+', '
     C                                    +%CHAR(A20_AR2(2))+', '
     C                                    +%CHAR(A20_AR2(3))+', '
     C                                    +%CHAR(A20_AR2(4))+', '
     C                                    +%CHAR(A20_AR2(5))+', '
     C                                    +%CHAR(A20_AR2(6))+')'
     C                                    +' Div('
     C                                    +%CHAR(A20_AR3(1))+', '
     C                                    +%CHAR(A20_AR3(2))+', '
     C                                    +%CHAR(A20_AR3(3))+', '
     C                                    +%CHAR(A20_AR3(4))+', '
     C                                    +%CHAR(A20_AR3(5))+', '
     C                                    +%CHAR(A20_AR3(6))+')'
     C     £DBG_Str      DSPLY
