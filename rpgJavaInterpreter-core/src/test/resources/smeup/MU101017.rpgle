     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 24/07/24  MUTEST  APU001 Creazione
     V* 25/07/24  MUTEST  APU001 Correzione del PGM e PAS.
     V* 25/07/24  MUTEST  APU001 Aggiunta l'assegnazione di un valore
     V*                          pari a 0 come fattore 2.
     V*=====================================================================
    O *  OBIETTIVO
    O * Utilizzo di MOVEL con un indicatore come risultato.
     V* ==============================================================
     D A10_A1          S              1  0
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU101017'
     C                   EVAL      £DBG_Sez = 'A10'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A10
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico MOVEL con indicatore come risultato
      *---------------------------------------------------------------------
     C     SEZ_A10       BEGSR
    OA* A£.CDOP(MOVEL )
     C                   EVAL      £DBG_Pas='P17'
      *
     C                   SETOFF                                           36
     C                   EVAL      £DBG_Str = '*IN36: ' + %CHAR(*IN36) + ';'
     C                   EVAL      A10_A1 = 1
     C                   MOVEL     A10_A1        *IN36
     C                   EVAL      £DBG_Str = £DBG_Str
     C                                        + '*IN36: ' + %CHAR(*IN36) + ';'
     C                   EVAL      A10_A1 = 0
     C                   MOVEL     A10_A1        *IN36
     C                   EVAL      £DBG_Str = £DBG_Str
     C                                        + '*IN36: ' + %CHAR(*IN36) + '.'
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C