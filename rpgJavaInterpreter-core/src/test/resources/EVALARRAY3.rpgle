     D A_STR1          S              1    DIM(3)
     D A_STR2          S              2    DIM(4)
     D A_INT1          S              1  0 DIM(3)
     D A_INT2          S              1  0 DIM(4)
      *
     D RES_S           S              2    DIM(4)
     D RES_I           S              1  0 DIM(4)
      *
     D £DBG_Str        S             52
      *
     C                   EVAL      A_STR1(1)='A'
     C                   EVAL      A_STR1(2)='B'
     C                   EVAL      A_STR2(3)='C'
     C                   EVAL      A_STR2(4)='D'
     C                   EVAL      RES_S=A_STR1+A_STR2
     C                   EVAL      £DBG_Str='1('+RES_S(1)+')'
     C                                    +' 2('+RES_S(2)+')'
     C                                    +' 3('+RES_S(3)+')'
     C                                    +' 4('+RES_S(4)+')'
      * Expect '1(A ) 2(B ) 3( C) 4(  )'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      A_INT1(1)=1
     C                   EVAL      A_INT1(2)=2
     C                   EVAL      A_INT2(2)=2
     C                   EVAL      A_INT2(3)=3
     C                   EVAL      RES_I=A_INT1+A_INT2
     C                   EVAL      £DBG_Str='1('+%CHAR(RES_I(1))+')'
     C                                    +' 2('+%CHAR(RES_I(2))+')'
     C                                    +' 3('+%CHAR(RES_I(3))+')'
     C                                    +' 4('+%CHAR(RES_I(4))+')'
      * Expect '1(1) 2(4) 3(3) 4(0)'
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                          LR