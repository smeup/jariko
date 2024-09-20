     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 19/09/24  V6R1   BMA Creato
     V*=====================================================================
    O * OBIETTIVO
     FMULANGTL  IF   E           K DISK
      *
     DMULANG         E DS                  EXTNAME(MULANGTF)
    O *  Verificare assegnazioni numeri packed e zoned sia in DS che in campi stand alone
      *
     D A10_DS_P01      DS
     D  A10_DS_P01_A                  5    INZ('A')
      * Numero zoned
     D  A10_DS_P01_B                  4  0 INZ(44)
      * Numero zoned
     D  A10_DS_P01_C                  5  2 INZ(5,51)
      * Numero zoned
     D  A10_DS_P01_D                  7S 2 INZ(7,71)
      *
      * Copia qualificata della DS
     D A10_DL_P01      DS                  LIKEDS(A10_DS_P01)
      * Stringa con lunghezza pari alla DS
     D A10_ST_P01      S                   LIKE(A10_DS_P01)
      *
     D A10_DS_P02      DS
     D  A10_DS_P02_A                  5    INZ('A')
      * Numero packed
     D  A10_DS_P02_B                  4P 0 INZ(44)
      * Numero packed
     D  A10_DS_P02_C                  5P 2 INZ(4,41)
      * Numero zoned
     D  A10_DS_P02_D                  7  2 INZ(7,71)
      *
      * Copia qualificata della DS
     D A10_DL_P02      DS                  LIKEDS(A10_DS_P02)
      * Stringa con lunghezza pari alla DS
     D A10_ST_P02      S                   LIKE(A10_DS_P02)
      *
      * Numero packed
     D  A10_P03_A      S              5P 2 INZ(4,41)
      * Numero packed
     D  A10_P03_B      S              5  2 INZ(5,41)
      * Numero zoned
     D  A10_P03_C      S              5S 2 INZ(7,41)
      *
     D  AAA020         S             20
     D  BBB020         S             20
     D  CCC020         S             20
      *---------------------------------------------------------------------
    RD* MAIN
      *---------------------------------------------------------------------
      * Esempi con DS contenente campi zoned
      * . Eval di una  DS con una DS contenente campi zoned
     C                   EVAL      A10_DL_P01=A10_DS_P01
      * . Movel di una  DS con una DS contenente campi zoned
     C                   MOVEL     A10_DS_P01    A10_DL_P01
     C                   MOVEL(P)  A10_DS_P01    A10_DL_P01
      * . Eval di una stringa con una DS contenente campi zoned
     C                   EVAL      A10_ST_P01=A10_DS_P01
      * . Movel di una stringa con una DS contenente campi zoned
     C                   MOVEL     A10_DS_P01    A10_ST_P01
     C                   MOVEL(P)  A10_DS_P01    A10_ST_P01
      * . Movel di una DS con una stringa contenente campi zoned
     C                   MOVEL     A10_ST_P01    A10_DS_P01
     C                   MOVEL(P)  A10_ST_P01    A10_DS_P01
      *
      * Esempi con DS contenente campi packed
      * . Eval di una  DS con una DS contenente campi packed
     C                   EVAL      A10_DL_P02=A10_DS_P02
      * . Movel di una  DS con una DS contenente campi packed
     C                   MOVEL     A10_DS_P02    A10_DL_P02
     C                   MOVEL(P)  A10_DS_P02    A10_DL_P02
      * . Eval di una stringa con una DS contenente campi packed
      * .. Questo esempio deve dare eccezione
     C                   EVAL      A10_ST_P02=A10_DS_P02
      * . Movel di una stringa con una DS contenente campi packed
      * .. Questo esempio deve dare eccezione
     C                   MOVEL     A10_DS_P02    A10_ST_P02
      * .. Questo esempio deve dare eccezione
     C                   MOVEL(P)  A10_DS_P02    A10_ST_P02
      * . Eval di una DS contenente campi packed con una stringa
      * .. Questo esempio deve dare eccezione
     C                   EVAL      A10_DS_P02=A10_ST_P02
      * . Movel di una DS con una stringa contenente campi zoned
      * .. Questo esempio deve dare eccezione
     C                   MOVEL     A10_ST_P02    A10_DS_P02
      * .. Questo esempio deve dare eccezione
     C                   MOVEL(P)  A10_ST_P02    A10_DS_P02
      *
      * Esempi con campi stand alone
     C                   EVAL      A10_P03_A=10*A10_P03_A
     C                   EVAL      A10_P03_B=20*A10_P03_B
     C                   EVAL      A10_P03_C=30*A10_P03_C
     C                   MOVEL     A10_P03_A     AAA020
     C                   MOVEL     AAA020        A10_P03_A
     C                   MOVEL     A10_P03_B     BBB020
     C                   MOVEL     BBB020        A10_P03_B
     C                   MOVEL     A10_P03_C     CCC020
     C                   MOVEL     CCC020        A10_P03_C
     C                   CLEAR                   CCC020
     C                   MOVEL     CCC020        A10_P03_C
      *
     C                   EVAL      MLSYST='KOKOS'
     C     MLSYST        CHAIN     MULANGTL
     C     MLTIPO        DSPLY
     C     MLPROG        DSPLY
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------------