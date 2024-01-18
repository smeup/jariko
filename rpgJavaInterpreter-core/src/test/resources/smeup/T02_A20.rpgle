     D A20_Z1          S               Z
     D A10_Z2          S               Z   INZ(Z'2003-06-27-09.25.59.123456')
     D £DBG_Str        S             52
      *
     C                   EVAL      A20_Z1=%TIMESTAMP()
     C                   EVAL      £DBG_Str='A20_Z1('+%CHAR(A20_Z1)+')'
      * Expected 'A20_Z1(xxxxxxxxxxxxxxxxxxxxxxxxxx)' actual value of timestamp
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      £DBG_Str='A20_Z2('+%CHAR(A10_Z2)+')'
      * Expected 'A20_Z2(2003-06-27-09.25.59.123456)'
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR