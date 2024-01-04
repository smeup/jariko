     FEMPLOYEE  IF   E           K DISK
      **************************************************************************
     D £DBG_Str        S           2560                                         Stringa
     D £DBG_Pas        S             10                                         Passo
      **************************************************************************
    OA* A£.CDOP(OPEN;CLOSE) A&.BIFN(%OPEN)
     D* Apri un file, leggi e chiudi.
     C                   EVAL      £DBG_Pas='P01'
     C                   EVAL      £DBG_Str='TEST_%OPEN:'
     C                   IF        %OPEN(EMPLOYEE)
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'FILE_JUST_OPENED::'
     C                   ELSE
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'FILE_CLOSE::'
     C                   ENDIF
      *
     C                   OPEN      EMPLOYEE
     C                   IF        %OPEN(EMPLOYEE)
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'FILE_OPEN::'
     C                   ENDIF
      *
     C                   CLOSE     EMPLOYEE
     C                   IF        NOT %OPEN(EMPLOYEE)
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'FILE_CLOSE::'
     C                   ENDIF
      *
     C                   SETON                                        LR