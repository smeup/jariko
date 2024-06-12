      *====================================================================
      * smeup V6R1.023DV
      * Nome sorgente       : MU604002
      * Sorgente di origine : QTEMP/SRC(MU604002)
      * Esportato il        : 20240612 092228
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 30/05/24  MUTEST  GUAGIA Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * Campo definito sia nel formato del file video che in line
     V* ==============================================================
     FMLNGT60CV CF   E             WORKSTN USROPN
      *
     D $OP             S              1N   INZ(*ON)
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
     IFMT1
     I*              W$RISC                      AAAAAA
     I              W$D001                      AAAAAA
     I              W$D002                      BBBBBB
     I              W$D003                      CCCCCC
     I              W$D004                      DDDDDD
     I              W$D005                      EEEEEE
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU604002'
     C                   EVAL      £DBG_Sez = 'A40'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A40
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico SFILE
      *---------------------------------------------------------------------
     C     SEZ_A40       BEGSR
    OA* A£.CDOP(SFILE   )
     D* Campo definito sia nel formato del file video che in line
     C                   EVAL      £DBG_Pas='P02'
     C                   IF        $OP=*OFF
     C                   OPEN      MLNGT60CV
     C                   ENDIF
     C                   IF        $OP=*OFF
     C                   EXFMT     FMT1
     C                   ENDIF
     C                   EVAL      £DBG_Str='FMT01 :: ' +
     C*                               'AAAAAA(' + %TRIM(AAAAAA) + ') '
     C                               'AAAAAA(' + %TRIM(AAAAAA) + ') ' +
     C                               'BBBBBB(' + %TRIM(BBBBBB) + ') ' +
     C                               'CCCCCC(' + %TRIM(CCCCCC) + ') ' +
     C                               'DDDDDD(' + %TRIM(DDDDDD) + ') ' +
     C                               'EEEEEE(' + %TRIM(EEEEEE) + ')'
     C                   IF        $OP=*OFF
     C                   CLOSE     MLNGT60CV
     C                   ENDIF
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C
