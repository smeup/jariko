     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 23/05/24  MUTEST  APU001 Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * Inizializzazione di un campo con la dimensione della DS
    O *  cui ne fa parte.
    O * In questo caso vengono utilizzate le posizioni specifiche
     V* ==============================================================
     DA40_DS1          DS
     D DS1_FL1                 1      4B 0 INZ(%SIZE(A40_DS1))
     D DS1_FL2                 5      8B 0
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU024013'
     C                   EVAL      £DBG_Sez = 'A40'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A40
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico INZ con SIZE e FROM
      *---------------------------------------------------------------------
     C     SEZ_A40       BEGSR
    OA* A£.TPDA(DS  )
     C                   EVAL      £DBG_Pas='P13'
     C                   EVAL      £DBG_Str='Size: ' + %CHAR(DS1_FL1)
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C