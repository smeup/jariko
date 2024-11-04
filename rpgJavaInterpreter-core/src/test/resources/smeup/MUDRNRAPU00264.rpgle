      * Test declaration
     D SDECT1          S                   LIKE(£DECTP)

      * Using LIKE on a definition that is defined instatement inside an API should resolve correctly (See SDECT1)
     C/API QILEGEN,CALLDEC

      * Test output
     C                   EVAL   SDECT1 = 'ok'
     C     SDECT1        DSPLY
