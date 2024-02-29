     D A35_A20A        S             20    INZ('!ABC!ABCDEF')
     D £DBG_Str        S             100

     D* SCAN senza variabile
     C                   SETOFF                                           20
     C     '!'           SCAN      A35_A20A                               20
     C                   EVAL      £DBG_Str='Src1='+%CHAR(*IN20)
     C     '?'           SCAN      A35_A20A                               20
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)
     C                                 +' Src2='+%CHAR(*IN20)
     C     £DBG_Str      DSPLY