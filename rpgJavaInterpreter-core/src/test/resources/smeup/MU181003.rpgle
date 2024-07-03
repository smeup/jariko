     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 03/07/24  MUTEST  APU001 Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * Testare il funzionamento della definizione di una variabile,
    O *  all'interno di una procedura, mediante LIKE verso una presente
    O *  in una COPY.
    O * Questa COPY sarà presente all'interno della procedura stessa.
    O * A tal proposito, quest'ultima non dovrà contenere specifiche D,
    O *  ma dichiarazioni inline di specifiche C.
     V* ==============================================================
     D A10_S10         S             10
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU181003'
     C                   EVAL      £DBG_Sez = 'A10'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A10
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico LIKE ad una variabile in COPY
      *---------------------------------------------------------------------
     C     SEZ_A10       BEGSR
    OA* A£.CDOP(LIKE)
     C                   EVAL      £DBG_Pas='P03'
     C                   EVAL      A10_S10 = '   HT   '
     C                   EVAL      £DBG_Str = 'O:' +
     C                              A10_S10 + '-P:' +
     C                              %TRIMR(A10_PR03())
     C                   ENDSR
      /COPY QILEGEN,MULANG_D_C
      *---------------------------------------------------------------------
     P A10_PR03        B
     D A10_PR03        PI            10
     D A10_PR03_V1     S                   LIKE(£RSSPG)
     C                   EVAL      A10_PR03_V1 = A10_S10
     C                   RETURN    %TRIM(A10_PR03_V1) + '_P'
      /COPY QILEGEN,£RISBS
     P A10_PR03        E
      *---------------------------------------------------------------------