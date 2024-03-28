     D £DBG_Str        S            150          VARYING
     D T11_A10_A20A    S              2  0 INZ(10)
     D T11_A10_A20B    S              2  0 INZ(0)
     C                   MONITOR
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'BLOCCO1;'
     C                   MONITOR
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+' BLOCCO2;'
     C                   MONITOR
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+' BLOCCO3'
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'; '+
     C                               %CHAR(T11_A10_A20A/T11_A10_A20B)
     C                   ON-ERROR
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                '; ERR_ZERO_DIV;'
     C                   ENDMON
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+' FINE_BLOCCO3;'
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'; '+
     C                               %CHAR(T11_A10_A20A/T11_A10_A20B)
     C                   ON-ERROR
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                '; ERR_ZERO_DIV;'
     C                   ENDMON
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+' FINE_BLOCCO2;'
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'; '+
     C                               %CHAR(T11_A10_A20A/T11_A10_A20B)
     C                   ON-ERROR
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                '; ERR_ZERO_DIV;'
     C                   ENDMON
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+' FINE_BLOCCO1;'
     C     £DBG_Str      DSPLY
