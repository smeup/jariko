      * Test declaration
     D                 DS
     D  SSERIE                1    500

      * Using %LEN on a definition inside a DIM should resolve appropriately
     D $SER            S              3  0 DIM(%LEN(SSERIE))
     C                   EVAL      $SER=0

      * Test output
     C     $SER          DSPLY
