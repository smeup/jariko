      * Test fields
     D  A10_P03_A      S              5P 2 INZ(4,41)
     D  A10_P03_B      S              5  2 INZ(5,41)
     D  A10_P03_C      S              5S 2 INZ(7,41)

     D  AAA020         S             20
     D  BBB020         S             20
     D  CCC020         S             20

      * Number assignment with standalone fields
     C                   EVAL      A10_P03_A=10*A10_P03_A
     C     A10_P03_A     DSPLY
     C                   EVAL      A10_P03_B=20*A10_P03_B
     C     A10_P03_B     DSPLY
     C                   EVAL      A10_P03_C=30*A10_P03_C
     C     A10_P03_C     DSPLY
     C                   MOVEL     A10_P03_A     AAA020
     C     AAA020        DSPLY
     C                   MOVEL     AAA020        A10_P03_A
     C     A10_P03_A     DSPLY
     C                   MOVEL     A10_P03_B     BBB020
     C     BBB020        DSPLY
     C                   MOVEL     BBB020        A10_P03_B
     C     A10_P03_B     DSPLY
     C                   MOVEL     A10_P03_C     CCC020
     C     CCC020        DSPLY
     C                   MOVEL     CCC020        A10_P03_C
     C     A10_P03_C     DSPLY
     C                   CLEAR                   CCC020
     C     CCC020        DSPLY
     C                   MOVEL     CCC020        A10_P03_C
     C     A10_P03_C     DSPLY
