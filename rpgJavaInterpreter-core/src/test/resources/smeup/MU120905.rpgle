     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 15/05/24  MUTEST  APU001 Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * L'obiettivo di questo test è quello di verificare
    O *  il funzionamento del DOUxx, in particolare del DOULT.
     V* ==============================================================
     DA09_N1           S              3I 0    INZ(10)
     DA09_N2           S              3I 0    INZ(0)
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU120905'
     C                   EVAL      £DBG_Sez = 'A09'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A09
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico DOULT
      *---------------------------------------------------------------------
     C     SEZ_A09       BEGSR
    OA* A£.COOP(DOULT)
     C                   EVAL      £DBG_Pas='P05'
      *
     C     A09_N1        DOULT     A09_N2
     C                   ADD       1             A09_N2
     C                   EVAL      £DBG_Str='A09_N2: ' + %CHAR(A09_N2)
     C                   ENDDO
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C
