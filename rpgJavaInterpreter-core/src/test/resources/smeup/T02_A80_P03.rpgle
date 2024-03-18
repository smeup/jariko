     D A80_A10         S             10
     D A80_N50         S              5  0
     D A80_NN3         S              3  0
     D £DBG_Str        S             50          VARYING

     C                   CLEAR                   A80_N30           3 0
     C     *LIKE         DEFINE    A80_NN3       A80_N30
     C                   EVAL      A80_N30 = 123
     C                   EVAL      £DBG_Str=%CHAR(A80_N30)
     C     £DBG_Str      DSPLY