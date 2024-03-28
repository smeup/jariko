     D A80_A10         S             10
     D A80_N50         S              5  0
     D A80_NN3         S              3  0
     D £DBG_Str        S             50          VARYING

     C                   CLEAR                   £DBG_Mdv         18
     C                   EVAL      £DBG_Mdv='PROVA'
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Mdv)
     C     £DBG_Str      DSPLY