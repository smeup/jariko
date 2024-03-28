     D A10_PR02        PR            25A
     D A10_PR02_V1                          CONST LIKE(T10_A10_A20)
     D T10_A10_A20     S             25
     D £DBG_Str        S             50          VARYING

     C                   EVAL      T10_A10_A20='TestProcedura'
     C                   EVAL      £DBG_Str=A10_PR02(T10_A10_A20)
     C     £DBG_Str      DSPLY

     P A10_PR02        B
     D A10_PR02        PI            25A
     D A10_PR02_V1                          CONST LIKE(T10_A10_A20)
     C                   RETURN    %TRIM(A10_PR02_V1)+'_Ritorno'
     P A10_PR02        E