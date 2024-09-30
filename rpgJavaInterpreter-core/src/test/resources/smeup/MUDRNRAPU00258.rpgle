      * Test declarations
     D A10_DS_P01      DS
     D  A10_DS_P01_A                  5    INZ('A')
      * Zoned number
     D  A10_DS_P01_B                  4  0 INZ(44)
      * Zoned number
     D  A10_DS_P01_C                  5  2 INZ(5,51)
      * Zoned number
     D  A10_DS_P01_D                  7S 2 INZ(7,71)
      * Qualified copy of a DS
     D A10_DL_P01      DS                  LIKEDS(A10_DS_P01)

      * . Eval of a DS with a DS containing zoned fields
     C                   EVAL      A10_DL_P01=A10_DS_P01
     C     A10_DL_P01    DSPLY
      * . Movel of a DS with a DS containing zoned fields
     C                   MOVEL     A10_DS_P01    A10_DL_P01
     C     A10_DL_P01    DSPLY
     C                   MOVEL(P)  A10_DS_P01    A10_DL_P01
     C     A10_DL_P01    DSPLY