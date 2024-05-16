      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : MU711002
      * Sorgente di origine : QTEMP/SRC(MU711002)
      * Esportato il        : 20240409 121031
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 23/03/24  MUTEST  GUAGIA Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * Testare la sostituzione di /API con /COPY
     V* ==============================================================
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU711002'
     C                   EVAL      £DBG_Sez = 'A10'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A10
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico /COPY £OAV
      *---------------------------------------------------------------------
     C     SEZ_A10       BEGSR
    OA* A£.CDOP(/API)
     D* Testare la sostituzione di /API con /COPY nel caso £DEC
     C                   EVAL      £DBG_Pas='P02'
     C                   EVAL      £DECTP='D8'
     C                   EVAL      £DECPA='*DMY'
     C                   EVAL      £DECCD='250324'
     C                   EXSR      £DEC
     C                   EVAL      £DBG_Str='Decodifica base: '+%TRIM(£DECDE)
     C                              +'Decodifica completa: '+%TRIM(£DECO_DESC)
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C
      /API QILEGEN,£DEC
