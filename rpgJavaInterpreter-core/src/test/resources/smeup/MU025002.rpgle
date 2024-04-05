      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : MU025002
      * Sorgente di origine : QTEMP/SRC(MU025002)
      * Esportato il        : 20240403 145832
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 25/03/24  MUTEST  GUAGIA Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * DEFINE da variabile dichiarata con DEFINE
     V* ==============================================================
     D A50_A1          S              7
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU025002'
     C                   EVAL      £DBG_Sez = 'A50'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A50
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico DS
      *---------------------------------------------------------------------
     C     SEZ_A50       BEGSR
    OA* A£.CDOP(DEFINE  )
     D* DEFINE da variabile dichiarata con DEFINE
     C                   EVAL      £DBG_Pas='P02'
     C     *LIKE         DEFINE    A50_A1        A50_A3
     C     *LIKE         DEFINE    A50_A3        A50_A4
     C                   EVAL      £DBG_Str= 'A50_A3('+A50_A3+')'
     C                                     +' A50_A4('+A50_A4+')'
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C
