      * Declarations needed for test purpose
     D £UIBDS          DS            10
     D £UIB35                          0

      * Our compiler should be able to coerce boolean values to unlimited strings
      * like it already does to normal strings
     C                   EVAL      £UIB35=*OFF

      * Test output
     C     £UIB35        DSPLY