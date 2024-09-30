      * Test declarations
     D A10_DS_P02      DS
     D  A10_DS_P02_A                  5    INZ('A')
      * Packed number
     D  A10_DS_P02_B                  4P 0 INZ(44)
      * Packed number
     D  A10_DS_P02_C                  5P 2 INZ(4,41)
      * Packed number
     D  A10_DS_P02_D                  7  2 INZ(7,71)

      * Qualified copy of the DS
     D A10_DL_P02      DS                  LIKEDS(A10_DS_P02)

      * . Eval of a DS with a DS containing packed fields
     C                   EVAL      A10_DL_P02=A10_DS_P02
     C     A10_DL_P02    DSPLY
      * . Movel of a DS with a DS containing packed fields
     C                   MOVEL     A10_DS_P02    A10_DL_P02
     C     A10_DL_P02    DSPLY
     C                   MOVEL(P)  A10_DS_P02    A10_DL_P02
     C     A10_DL_P02    DSPLY