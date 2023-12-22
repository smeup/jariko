     D AR1_5           S              1    DIM(5)
     D AR1_20          S              1    DIM(20)
     D AR2_5           S              2    DIM(5)
     D £DBG_Str        S             52
      *
      * Assing Array/Array with same field length. Left array dim > Right array dim
      * The array on the right is correctly copied
     C                   EVAL      AR1_5(1)='A'
     C                   EVAL      AR1_5(2)='B'
     C                   EVAL      AR1_20=AR1_5
     C                   EVAL      £DBG_Str='1('+AR1_20(1)+')'
     C                                    +' 2('+AR1_20(2)+')'
     C                                    +' 6('+AR1_20(6)+')'
      * Expected '1(A) 2(B) 6( )'
     C     £DBG_Str      DSPLY
      *
     C                   CLEAR                   AR1_5
     C                   CLEAR                   AR1_20
      *
      * Assing Array/Array with same field length. Left array dim < Right array dim
      * Only fields with index less than DIM are copied
     C                   EVAL      AR1_20(1)='A'
     C                   EVAL      AR1_20(2)='B'
     C                   EVAL      AR1_20(5)='E'
     C                   EVAL      AR1_20(6)='F'
     C                   EVAL      AR1_5=AR1_20
     C                   EVAL      £DBG_Str='1('+AR1_5(1)+')'
     C                                    +' 2('+AR1_5(2)+')'
     C                                    +' 5('+AR1_5(5)+')'
      * Expected '1(A) 2(B) 5(E)'
     C     £DBG_Str      DSPLY
      *
     C                   CLEAR                   AR1_5
     C                   CLEAR                   AR2_5
      *
      * Assing Array/Array with different field length. Left field length > Right field length
      * The array on the right is correctly copied
     C                   EVAL      AR1_5(1)='A'
     C                   EVAL      AR1_5(2)='B'
     C                   EVAL      AR2_5=AR1_5
     C                   EVAL      £DBG_Str='1('+AR2_5(1)+')'
     C                                    +' 2('+AR2_5(2)+')'
      * Expected '1(A ) 2(B )'
     C     £DBG_Str      DSPLY
      *
     C                   CLEAR                   AR1_5
     C                   CLEAR                   AR2_5
      *
      * Assing Array/Array with different field length. Left field length < Right field length
      * Felds are copied but trunched based on field length
     C                   EVAL      AR2_5(1)='AZ'
     C                   EVAL      AR2_5(2)='BY'
     C                   EVAL      AR1_5=AR2_5
     C                   EVAL      £DBG_Str='1('+AR1_5(1)+')'
     C                                    +' 2('+AR1_5(2)+')'
      * Expected '1(A) 2(B)'
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR