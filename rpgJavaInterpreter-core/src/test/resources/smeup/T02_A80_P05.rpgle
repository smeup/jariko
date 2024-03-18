     DXQNCOG         E DS                  EXTNAME(CQNCOG0F) INZ
     D £DBG_Str        S             50          VARYING

     C                   CLEAR                   X§PROG           10
     C                   EVAL      X§PROG = 'ABCDEFGHIJ'
     C                   EVAL      £DBG_Str=%TRIM(X§PROG)
     C     £DBG_Str      DSPLY