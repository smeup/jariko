     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 24/05/24  MUTEST  APU001 Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * SCAN di `:`
     V* ==============================================================
     D* Sezione delle variabili.
     DA35_A15_1        S             15    INZ('AA:BB')
     DA35_A15_2        S              5U 0
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU103511'
     C                   EVAL      £DBG_Sez = 'A35'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A35
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico SCAN con di `:`
      *---------------------------------------------------------------------
     C     SEZ_A35       BEGSR
    OA* A£.COOP(SCAN)
     C                   EVAL      £DBG_Pas='P11'
      *
     C     ':'           SCAN      A35_A15_1:1   A35_A15_2
     C                   EVAL      £DBG_Str='Found at: ' + %CHAR(A35_A15_2)
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C