     D £DBG_Str        S            100
     D £DBG_Pgm        S             15
     D £DBG_Sez        S             15
     D £DBG_Fun        S             15
     D £DBG_Pas        S             15
      * --------------------------------------------------------------
      */COPY QILEGEN,MULANG_D_D
      */COPY QILEGEN,£TABB£1DS
      */COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU701008'
     C                   EVAL      £DBG_Sez = 'A70'
     C                   EVAL      £DBG_Fun = '*INZ'
     C*                  EXSR      £DBG
     C                   EXSR      SEZ_A70
     C*                  EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C*                  EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test direttive di compilazione annidate
      *---------------------------------------------------------------------
     C     SEZ_A70       BEGSR
    OA* A£.CDOP(COMDIRNES)
     D* Double ELSE
     C                   EVAL      £DBG_Pas='P08'
      /IF DEFINED(DEFINE_1)
      /ELSE
      /ELSE
      /ENDIF
     C                   ENDSR
      *---------------------------------------------------------------------
      */COPY QILEGEN,MULANG_D_C
