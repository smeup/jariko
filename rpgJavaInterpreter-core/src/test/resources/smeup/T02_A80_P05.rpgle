     DXQNCOG         E DS                  EXTNAME(CQNCOG0F) INZ
     DCQNCOG         E DS                  EXTNAME(CQNCOG0F) INZ
     D £DBG_Str        S             50          VARYING

     C                   CLEAR                   X§PROG           10
     C                   CLEAR                   X§QT01           15 5
     C                   EVAL      X§PROG = 'ABCDEFGHIJ'
     C                   EVAL      X§QT01 =  123
     C                   EVAL      £DBG_Str=%TRIM(X§PROG)+%CHAR(X§QT01)
     C     £DBG_Str      DSPLY