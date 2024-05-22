      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : MU021008
      * Sorgente di origine : QTEMP/SRC(MU021008)
      * Esportato il        : 20240429 170424
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 29/04/24  MUTEST  COSANT Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * Test costante con su 2 righe
     V* ==============================================================
     D* Sezione delle variabili. Per esempio:
     D UP              C                   CONST('ABCDEFGHIJKLMNOPQRST-         _NOTXT
     D                                     UVWXYZ')
     D LO              C                   CONST('abcdefghijklmnopqrst-         _NOTXT
     D                                     uvwxyz')
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£G40E
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU021008'
     C                   EVAL      £DBG_Sez = 'A10'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A10
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
     C     SEZ_A10       BEGSR
    OA* A£.TPDA(C)
     D* Passo
     C                   EVAL      £DBG_Pas='P08'
      * Assegnazione della costante
     C                   EVAL      £DBG_Str = UP
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C
