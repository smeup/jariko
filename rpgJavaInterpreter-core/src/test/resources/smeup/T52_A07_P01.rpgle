     FMULANGTL  UF A E           K DISK

     D KEY_A10C_SYST   S             10    INZ('IBN')
     D KEY_A10C_TIPO   S             10    INZ('3')
     D KEY_A10C_PROG   S             10    INZ('MULANGXX')

     D A10C_PSEZ       S             10
     D A10C_PPAS       S             10
     D SEZ_52_INDICE   S             4  0
     D £DBG_Str        S             100          VARYING

     D* Scrittura nuove righe.
     C                   FOR       SEZ_52_INDICE=0 TO 10
     C                   EVAL      A10C_PSEZ='A'+
     C                                 %SUBST(%EDITC(SEZ_52_INDICE:'X'):1:2)
     C                   EVAL      A10C_PPAS='P'+
     C                                 %SUBST(%EDITC(SEZ_52_INDICE:'X'):3:2)
     C                   EVAL      MLSYST=%TRIM(KEY_A10C_SYST)
     C                   EVAL      MLTIPO=%TRIM(KEY_A10C_TIPO)
     C                   EVAL      MLPROG=%TRIM(KEY_A10C_PROG)
     C                   EVAL      MLPSEZ=%TRIM(A10C_PSEZ)
     C                   EVAL      MLPPAS=%TRIM(A10C_PPAS)
     C                   WRITE     MULANGR
     C                   ENDFOR

     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+':ENDWRT:'