      *---------------------------------------------------------------
      * Test operation that has to throws an assignment error
      * Ok decimal but wrong size
      *---------------------------------------------------------------
     D NUM1            S              3  1
     C                   EVAL      NUM1=99.9
     C     NUM1          DSPLY
      * I cannot assign because size is 4 digits
     C                   EVAL      NUM1=100.1
     C     NUM1          DSPLY