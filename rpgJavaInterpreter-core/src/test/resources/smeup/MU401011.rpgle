     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 19/04/24  MUTEST  APU001 Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * L'obiettivo di questo test è l'utilizzo di OCCURS dove, tra le sue
    O *  parentesi, abbiamo una espressione da valutare.
     V* ==============================================================
     D* Sezione delle variabili.
     D D40_AR1         S             10I 0 DIM(10)
     D D40_DS1         DS                  OCCURS(%ELEM(D40_AR1))
     D  D40_DS1_I1                   10I 0
     D  D40_DS1_I2                   10I 0
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU401011'
     C                   EVAL      £DBG_Sez = 'A40'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A40
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico OCCURS ed espressione al suo interno
      *---------------------------------------------------------------------
     C     SEZ_A40       BEGSR
    OA* A£.TPDA(OCCURS)
     D* Commento
     C                   EVAL      £DBG_Pas='P11'
      *
     C                   EVAL      £DBG_Str= 'HELLOTHERE'
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C