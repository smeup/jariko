     D A80_B01         S              1N
     D £DBG_Str        S             50          VARYING

     C                   CLEAR                   A80_B01           1
     C                   EVAL      A80_B01 = *ON
     C                   EVAL      £DBG_Str=%CHAR(A80_B01)
     C     £DBG_Str      DSPLY