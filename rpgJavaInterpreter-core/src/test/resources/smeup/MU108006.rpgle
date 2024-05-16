      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : MU108006
      * Sorgente di origine : QTEMP/SRC(MU108006)
      * Esportato il        : 20240415 082654
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 22/03/24  MUTEST  GUAGIA Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * CALL con parametri definiti in line e in specifiche D
     V* ==============================================================
     D FileDS          DS                  QUALIFIED
     D  File                         50
      *
     DP_PROCEDTST      PR                  LIKEDS(FileDS)
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU108006'
     C                   EVAL      £DBG_Sez = 'A80'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A80
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico LIKEDS
      *---------------------------------------------------------------------
     C     SEZ_A80       BEGSR
    OA* A£.CDOP(LIKEDS)
     D* CALL con LIKEDS
     C                   EVAL      £DBG_Pas='P06'
     C                   EVAL      FileDS=P_PROCEDTST()
     C                   EVAL      £DBG_Str=%TRIM(FileDS.File)
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C
    RD* Procedura di test con LIKEDS
      *---------------------------------------------------------------------
     PP_PROCEDTST      B
     D P_PROCEDTST     Pi                  LIKEDS(FileDS)
     D FileOUT         DS                  LIKEDS(FileDS)
      *
     C                   EVAL      FileOUT.File='ScritturaInProcedura'
     C                   RETURN    FileOUT
      *
     PP_PROCEDTST      E
      *---------------------------------------------------------------------
