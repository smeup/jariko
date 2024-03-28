     D £DBG_Str        S            150          VARYING
     D T11_A10_A20A    S              2  0 INZ(10)
     D T11_A10_A20B    S              2  0 INZ(0)
     C                   MONITOR
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'BLOCCO'
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'; '+
     C                               %CHAR(T11_A10_A20A/T11_A10_A20B)
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'; FINE_BLOCCO;'
     C                   ON-ERROR
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                '; ERR_ZERO_DIV;'
     C                   ENDMON
     C     £DBG_Str      DSPLY