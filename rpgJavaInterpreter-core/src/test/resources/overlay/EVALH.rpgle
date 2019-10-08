      *---------------------------------------------------------------
      * This test defines a data structure with two fields
      * It should possible to reference the fields using the
      * qualified name (dot notation).
      *---------------------------------------------------------------
     DA                S              5S 2
     DB                S              5S 2
     DRESULT           S              5S 2
      *---------------------------------------------------------------
      * M A I N
      *---------------------------------------------------------------
     C                   EVAL A = 139.0
     C                   EVAL B = 7.0
     C                   EVAL RESULT = A/B
     C                   DSPLY                   RESULT
     C                   EVAL(H)   RESULT = A/B
     C                   DSPLY                   RESULT
      *
     C                   SETON                                        RT
