     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 24/07/24  MUTEST  APU001 Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * Utilizzo di MOVE con un indicatore come risultato.
     V* ==============================================================
     D A10_A1          S              1  0
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU101016'
     C                   EVAL      £DBG_Sez = 'A10'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A10
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico MOVE con indicatore come risultato
      *---------------------------------------------------------------------
     C     SEZ_A10       BEGSR
    OA* A£.CDOP(MOVE  )
     C                   EVAL      £DBG_Pas='P16'
      *
     C                   EVAL      A10_A1 = 1
     C                   SETOFF                                           36
     C                   EVAL      £DBG_Str = '*IN36: ' + %CHAR(*IN36) + '; '
     C                   MOVE      A10_A1        *IN36
     C                   EVAL      £DBG_Str = £DBG_Str
     C                                        + '*IN36: ' + %CHAR(*IN36) + '.'
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C