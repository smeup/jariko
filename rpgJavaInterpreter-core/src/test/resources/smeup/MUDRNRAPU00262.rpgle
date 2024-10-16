      * Test declarations
     D RESULT          S              1  0

      * *ZERO/*ZEROS symbolic constant should be converted to numbers without any issue
     C                   EVAL      RESULT = %DEC('123.4':3:*ZEROS)

      * Test output
     C     RESULT        DSPLY