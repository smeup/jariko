     V* ==============================================================
     D* 24/09/24
     D* Purpose: Must fire error on assignment
     D* line 17.
     V* ==============================================================
     D A10_DS_P02      DS
     D  A10_DS_P02_A                  5    INZ('A')
      * Numero packed
     D  A10_DS_P02_B                  4P 0 INZ(44)
      * Numero packed
     D  A10_DS_P02_C                  5P 2 INZ(4,41)
      * Numero zoned
     D  A10_DS_P02_D                  7  2 INZ(7,71)

     D A10_ST_P02      S                   LIKE(A10_DS_P02)

     C                   EVAL      A10_DS_P02=A10_ST_P02