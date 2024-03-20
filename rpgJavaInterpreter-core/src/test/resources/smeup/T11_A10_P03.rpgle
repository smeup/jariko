     D £DBG_Str        S            180          VARYING
     D T11_A10_A20A    S              2  0 INZ(10)
     D T11_A10_A20B    S              2  0 INZ(0)
     C                   EVAL      £DBG_Str='DENTRO_IF('
     C                   IF        'A'='A'
     C                   EXSR      SUB_SEZ_A10
     C                   ENDIF
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+') DENTRO_DO('
     C                   DO        2
     C                   EXSR      SUB_SEZ_A10
     C                   ENDDO
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+') DENTRO_WHEN('
     C                   SELECT
     C                   WHEN      'A'='A'
     C                   EXSR      SUB_SEZ_A10
     C                   ENDSL
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+') DENTRO_OTHER('
     C                   SELECT
     C                   WHEN      'A'='B'
     C                   OTHER
     C                   EXSR      SUB_SEZ_A10
     C                   ENDSL
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+')'
     C     £DBG_Str      DSPLY

     C     SUB_SEZ_A10   BEGSR
     C                   MONITOR
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'BLOCCO'
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'; '+
     C                               %CHAR(T11_A10_A20A/T11_A10_A20B)
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'; FINE_BLOCCO;'
     C                   ON-ERROR
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                '; ERR_ZERO_DIV;'
     C                   ENDMON
     C                   ENDSR