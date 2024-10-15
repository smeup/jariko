      * Test declaration
     D $SER            S              3  0 DIM(%LEN(SSERIE))

     D STACK           DS                  OCCURS(300)
     D  STYP                   1      1
     D  SOP                    2     11
     D  SSERIE                12    511

      * Using %LEN on a definition inside a DIM should resolve appropriately
     C                   EVAL      $SER=0

      * Test output
     C     $SER          DSPLY
