     D A90_N1          S             20  0
     D £DBG_Str        S             52
      *
     C                   EVAL      A90_N1=123456
     C                   EVAL      £DBG_Str=%CHAR(A90_N1/1000)
      * Expected '123.4560000000'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      £DBG_Str=%CHAR(A90_N1/100)
      * Expected '1234.5600000000'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      A90_N1=123456000
     C                   EVAL      £DBG_Str=%CHAR(A90_N1/1000)
      * Expected '123456.0000000000'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      A90_N1=123456
     C                   EVAL      A90_N1=A90_N1/1000
     C                   EVAL      £DBG_Str=%CHAR(A90_N1)
      * Expected '123'
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR