     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 24/05/24  MUTEST  APU001 Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * L'obiettivo di questo test è quello di verificare
    O *  il funzionamento del DOUEQ all'interno di una SR con l'utilizzo
    O *  degli indicatori.
     V* ==============================================================
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU120907'
     C                   EVAL      £DBG_Sez = 'A09'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A09
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico DOUEQ con indicatori
      *---------------------------------------------------------------------
     C     SEZ_A09       BEGSR
    OA* A£.COOP(DOUEQ)
     C                   EVAL      £DBG_Pas='P07'
      *
     C                   SETOFF                                           55
     C     *IN55         DOUEQ     *ON
     C                   SETON                                            55
     C                   EVAL      £DBG_Str='HELLO THERE'
     C                   ENDDO
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C