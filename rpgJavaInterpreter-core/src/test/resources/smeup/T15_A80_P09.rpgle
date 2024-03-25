     D A80_V           S             40    VARYING INZ('North York')
     D A80_N           S              5  0
     D £DBG_Str        S            100           VARYING

      * Recupero lunghezza attuale
     C                   EVAL      A80_N = %LEN(A80_V)
     C                   EVAL      £DBG_Str='1('+%CHAR(A80_N)+' - '+A80_V+')'
      * Setto lunghezza a 5
     C                   EVAL      %LEN(A80_V) = 5
     C                   EVAL      A80_N= %LEN(A80_V)
     C                   EVAL      £DBG_Str=%TRIMR(£DBG_Str)
     C                                     +' 2('+%CHAR(A80_N)+' - '+A80_V+')'
      * Setto lunghezza a 15
     C                   EVAL      %LEN(A80_V) = 15
     C                   EVAL      A80_N= %LEN(A80_V)
     C                   EVAL      £DBG_Str=%TRIMR(£DBG_Str)
     C                                     +' 3('+%CHAR(A80_N)+' - '+A80_V+')'
     C     £DBG_Str      DSPLY
