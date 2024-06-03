     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 29/05/24  MUTEST  APU001 Creazione con sole definizioni
     V* 29/05/24  MUTEST  GUAGIA Aggiunta corpo C
     V*===============================================================
    O *  OBIETTIVO
    O *  Testare la creazione di una DS partendo da un metadata,
    O *   per poi aggiungere un field che fa la LIKE ad un suo campo
    O *   e con OVERLAY della DS.
     V* ==============================================================
     D A40_A20_10      S             20  6 DIM(10)
     DA40DS1         E DS                  EXTNAME(MULANGTF)
     D  DS1_FL1                                DIM(10) LIKE(MLASLA) INZ
     D                                         OVERLAY(A40DS1:2)
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU024014'
     C                   EVAL      £DBG_Sez = 'A40'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A40
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico EXTNAME e aggiunda field
      *---------------------------------------------------------------------
     C     SEZ_A40       BEGSR
    OA* A£.TPDA(DS  )
     C                   EVAL      £DBG_Pas='P14'
     C                   EVAL      A40DS1 = 'ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFG'+
     C                                      'HIJKLMNOPQRSTUVWXYZ'
     C                   EVAL      £DBG_Str = 'A40DS1(' + %TRIMR(A40DS1) +
     C                              ') DS1_FL1(1)(' + DS1_FL1(1) +
     C                              ') DS1_FL1(2)(' + DS1_FL1(2) + ')'
     C                   EVAL      DS1_FL1(1) = '88'
     C                   EVAL      £DBG_Str = %TRIMR(£DBG_Str) + ' | ' +
     C                              'A40DS1(' + %TRIMR(A40DS1) +
     C                              ') DS1_FL1(1)(' + DS1_FL1(1) +
     C                              ') DS1_FL1(2)(' + DS1_FL1(2) + ')'
     C                   EVAL      DS1_FL1(2) = '00'
     C                   EVAL      £DBG_Str = %TRIMR(£DBG_Str) + ' | ' +
     C                              'A40DS1(' + %TRIMR(A40DS1) +
     C                              ') DS1_FL1(1)(' + DS1_FL1(1) +
     C                              ') DS1_FL1(2)(' + DS1_FL1(2) + ')'
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C