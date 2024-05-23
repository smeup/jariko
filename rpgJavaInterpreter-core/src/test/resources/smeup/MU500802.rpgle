      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : MU500802
      * Sorgente di origine : QTEMP/SRC(MU500802)
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
     FMULANGTL  IF A E           K DISK
      *
     D OOSYST          S                   DIM(1000) LIKE(MLSYST)
     D DS01            DS                  OCCURS(%ELEM(OOSYST))
     D  SUBDS                         5
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU500802'
     C                   EVAL      £DBG_Sez = 'A08'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A08
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico
      *---------------------------------------------------------------------
     C     SEZ_A08       BEGSR
    OA* A£.CDOP()
     C                   EVAL      £DBG_Pas='P02'
     C                   EVAL      DS01.SUBDS='Test'
     C                   EVAL      £DBG_Str=%TRIM(DS01)
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C
