      * Helper declarations
     D £DBG_Str        S             2

      * Declarations needed for test purpose
     D £C6JDSO         DS           512
     D  £C6JO_TO                      2  0 DIM(10)
     D $X              S              5  0
     D §§IMPO          S              6  0
     D T$AG_I          S             21  6 DIM(15)

      * Initializing array indices to avoid null value exception
     C                   EVAL      $X=2
     C                   EVAL      £C6JO_TO($X)=2

      * Trying to access an array while accessing another array (like £C6JO_TO($X) inside T$AG_I).
      * £C6JO_TO($X) should not be seen as a function call but as an array access.
      * This should be true when this kind of array access is contained in any type expression.
     C                   EVAL      T$AG_I(£C6JO_TO($X))=
     C                             T$AG_I(£C6JO_TO($X))+§§IMPO

      * Test output
     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY
