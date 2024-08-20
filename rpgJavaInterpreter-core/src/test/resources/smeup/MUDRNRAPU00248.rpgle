      * Helper declarations
     D £DBG_Str        S             2

      * Declarations needed for test purpose
     FTABDS01L  IF   E           K DISK    USROPN

      * The '*START' symbolic constant should be correctly recognized.
     C     *START        SETLL     TABDS01L

      * The '*END' symbolic constant should be correctly recognized.
     C     *END          SETLL     TABDS01L

      * Test output
     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY