     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 18/08/24  MUTEST  APU001 Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * Esecuzione di una COPY il cui nome possiede dei caratteri
    O *  alfabetici in minuscolo.
     V* ==============================================================
      * --------------------------------------------------------------
     D A71_01          S                   LIKE(£K37I_ST)
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      /COPY QILEGEN,£k37DS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU711006'
     C                   EVAL      £DBG_Sez = 'A71'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A71
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* COPY case insensitive
      *---------------------------------------------------------------------
     C     SEZ_A71       BEGSR
    OA* A£.CDOP(/COPY)
     C                   EVAL      £DBG_Pas='P06'
      *
     C                   EVAL      A71_01='HELLO THERE'
     C                   EVAL      £DBG_Str=%TRIMR(A71_01)
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C