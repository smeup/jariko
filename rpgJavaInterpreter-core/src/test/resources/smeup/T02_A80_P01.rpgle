     D A80_A10         S             10
     D A80_N50         S              5  0
     D £DBG_Str        S             50          VARYING

     C                   CLEAR                   A80_A10          10
     C                   CLEAR                   A80_N50           5 0
     C                   EVAL      A80_A10 = 'ABCDEFGHIJ'
     C                   EVAL      A80_N50 =  12345
     C                   EVAL      £DBG_Str=%TRIM(A80_A10)+%CHAR(A80_N50)
     C     £DBG_Str      DSPLY