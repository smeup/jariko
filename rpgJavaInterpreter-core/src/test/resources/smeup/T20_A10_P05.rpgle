     D £DBG_Str        S           2560                                         Stringa
     D TXT             S             10
      * Test indicator 35 activation when CALL non existent pgm
     C                   CALL      'XXXXXX_YY'                          35
     C                   EVAL      £DBG_Str=%CHAR(*IN35)
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      TXT='PING'
     C                   CALL      'ECHO_PGM'                           35
     C                   PARM                    TXT
     C
     C                   EVAL      £DBG_Str=%CHAR(*IN35)
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR
