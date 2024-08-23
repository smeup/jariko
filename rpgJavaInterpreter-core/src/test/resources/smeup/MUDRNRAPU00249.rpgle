      * Helper declarations
     D £DBG_Str        S             2

      * Declaring an array field (£6I) in a data structure with an empty INZ keyword.
      * Should not be assigned to a numeric
     D £C6MIM          DS
     D  £6I                          20P 6 DIM(99) INZ                          Importi

      * Test output
     C                   EVAL      £DBG_Str='ok'
     C     £6I           DSPLY
