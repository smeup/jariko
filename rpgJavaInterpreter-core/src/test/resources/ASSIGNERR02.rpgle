      *---------------------------------------------------------------
      * Test operation that has to throws the assignment errors
      * Ok size but wrong decimal
      *---------------------------------------------------------------
     D NUM1            S              3  1
      * This assignment is right because 99.10 is the same of 99.1
     C                   EVAL      NUM1=99.10
     C     NUM1          DSPLY
      * I can assign because size is 4 digits
      * todo currently 99.11 is rounded to 99.1 but is it right?
     C                   EVAL      NUM1=99.11
     C     NUM1          DSPLY