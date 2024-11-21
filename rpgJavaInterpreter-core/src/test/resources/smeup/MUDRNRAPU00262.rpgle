      * Test declarations
     D RESULT          S              1  0

      * *ZERO/*ZEROS symbolic constant should be converted to numbers without any issue
      * TODO this test is wrong
      * The compiler says:
      * *******************************************************
      * Il terzo parametro *ZEROS per %DEC oppure %DECH non è
      * valido.
      * *******************************************************
     C                   EVAL      RESULT = %DEC('123.4':3:*ZEROS)

      * Test output
     C     RESULT        DSPLY