     D SCALA           S              3  0
     D A60_N30B        S              3  0
     D £DBG_Str        S             100          VARYING
     C                   EVAL      SCALA=10
     C                   EVAL      A60_N30B=24*SCALA+6
     C                   EVAL      £DBG_Str='Res(A*B+C)='+%CHAR(A60_N30B)
     C                   EVAL      A60_N30B = 24 * SCALA + 6
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)
     C                               +'; Res(A * B + C)='+%CHAR(A60_N30B)
     C     £DBG_Str      DSPLY