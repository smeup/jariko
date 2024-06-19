      *====================================================================
      * smeup V6R1.023DV
      * Nome sorgente       : MU500901
      * Sorgente di origine : QTEMP/SRC(MU500901)
      * Esportato il        : 20240612 092228
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 29/05/24  MUTEST  GUAGIA Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * Test atomico LIKE, OCCURS
     V* ==============================================================
     FMULANGTL  IF   E           K DISK
     F                                     RENAME(MULANGR:MULANG3)
      * -------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
     IMULANG3
     I              MLSYST                      TSSYST
     I              MLLIBR                      TSLIBR
     I              MLFILE                      TSFILE
     I              MLTIPO                      TATIPO
     I              MLPROG                      TBPROG
     I              MLPSEZ                      AAAAAA
     I              MLPPAS                      BBBBBB
     I              MLPDES                      CCCCCC
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU500901'
     C                   EVAL      £DBG_Sez = 'A09'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A09
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico
      *---------------------------------------------------------------------
     C     SEZ_A09       BEGSR
    OA* A£.CDOP()
     D* Test atomico specifiche I
     C                   EVAL      £DBG_Pas='P01'
      *
     C     TSLANG5K      KLIST
     C                   KFLD                    TSSYST
     C                   KFLD                    TATIPO
     C                   KFLD                    TBPROG
     C                   KFLD                    AAAAAA
     C                   KFLD                    BBBBBB
     C                   EVAL      TSSYST='IBMI'
     C                   EVAL      TATIPO='3'
     C                   EVAL      TBPROG='MULANGT12'
     C                   EVAL      AAAAAA='A03'
     C                   EVAL      BBBBBB='P01'
      *
     C                   EVAL      £DBG_Str=''
     C     TSLANG5K      SETLL     MULANGTL
     C                   READ      MULANG3
      *
     C                   EVAL      £DBG_Str='TSSYST(' + %TRIM(TSSYST) +
     C                              ') TSLIBR(' + %TRIM(TSLIBR) +
     C                              ') TSFILE(' + %TRIM(TSFILE) +
     C                              ') TATIPO(' + %TRIM(TATIPO) +
     C                              ') TBPROG(' + %TRIM(TBPROG) +
     C                              ') AAAAAA(' + %TRIM(AAAAAA) +
     C                              ') BBBBBB(' + %TRIM(BBBBBB) +
     C                              ') CCCCCC(' + %TRIM(CCCCCC) + ')'
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C
