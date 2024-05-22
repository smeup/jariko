      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : MU106011
      * Sorgente di origine : QTEMP/SRC(MU106011)
      * Esportato il        : 20240429 170425
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 29/04/24  MUTEST  GUAGIA Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O *
     V* ==============================================================
     D A60_D1          S             10
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU106011'
     C                   EVAL      £DBG_Sez = 'A60'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A60
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico LIKEDS
      *---------------------------------------------------------------------
     C     SEZ_A60       BEGSR
    OA* A£.CDOP(CALL;PLIST;PARM)
     D* CALL con parametri definiti in line e in specifiche D
     C                   EVAL      £DBG_Pas='P10'
     C                   EVAL      A60_D1='inp'
     C                   CALL      'MULANGTD10'
     C                   PARM                    A60_D1
     C                   EVAL      £DBG_Str='CALL('+A60_D1+')'
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C
