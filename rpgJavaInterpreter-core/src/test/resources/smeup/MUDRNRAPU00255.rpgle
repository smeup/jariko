      * Test declarations
     D A10_DS_P02      DS
     D  A10_DS_P02_A                  5    INZ('A')
      * Packed number
     D  A10_DS_P02_B                  4P 0 INZ(44)
      * Packed number
     D  A10_DS_P02_C                  5P 2 INZ(4,41)
      * Zoned number
     D  A10_DS_P02_D                  7  2 INZ(7,71)

     D A10_ST_P02      S                   LIKE(A10_DS_P02)

      * Assign a DS containing PACKED fields to a string
     C                   EVAL      A10_ST_P02=A10_DS_P02
     C     A10_ST_P02    DSPLY

     C                   MOVEL     A10_DS_P02    A10_ST_P02
     C     A10_ST_P02    DSPLY

     C                   MOVEL(P)  A10_DS_P02    A10_ST_P02
     C     A10_ST_P02    DSPLY