      * Helper declarations
     D £DBG_Str        S             2

      * Declarations needed for test purpose
     D C§IN            S              1N   DIM(40)
     D$C               S              5  0

      * Initializing array indices to avoid null value exception
     C                   EVAL      $C=2

      * Using an array access inside a NOT expression should be valid.
      * C§IN($C) must be properly detected as an array access expression.
      * Standalone C§IN($C) access must remain valid too.
     C                   IF        NOT(C§IN($C))
     C                   EVAL      C§IN($C)=*ON
     C                   ENDIF

      * Test output
     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY