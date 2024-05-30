     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 30/05/24  MUTEST  APU001 Creazione
     V*===============================================================
    O *  OBIETTIVO
    O * Testare la dichiarazione ed uso di una variabile di
    O *  tipo DATA (D), con visualizzazione del risultato.
     V* ==============================================================

     DA25_D1           S               D   DATFMT(*JUL) INZ(D'2024-05-30')
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU022501'
     C                   EVAL      £DBG_Sez = 'A25'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A25
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico dichiarazione ed uso di variabili DATA (D)
      *---------------------------------------------------------------------
     C     SEZ_A25       BEGSR
    OA* A£.TPDA(D)
     C                   EVAL      £DBG_Pas='P01'
     C     *JUL          MOVEL     A25_D1        DATE_JUL          8
     C     *ISO          MOVEL     A25_D1        DATE_ISO         12
     C                   EVAL      £DBG_Str='*JUL: ' + %TRIMR(DATE_JUL) +
     C                                      '; ' +
     C                                      '*ISO: ' + %TRIMR(DATE_ISO) +
     C                                      '.'
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C