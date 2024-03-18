     DCQNCOG         E DS                  EXTNAME(CQNCOG0F) INZ
     D £DBG_Str        S             50          VARYING

     C                   CLEAR                   N§PROG           10
     C                   EVAL      N§PROG = 'ABCDEFGHIJ'
     C                   EVAL      £DBG_Str=%TRIM(N§PROG)
     C     £DBG_Str      DSPLY