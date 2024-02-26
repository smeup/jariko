     D Msg             S             12
     D $$SVAR          S             20    INZ('xxx)yy')
     D $ATTRI          S              2  0
     D $BRACK          S              5  0
     C                   EVAL      $ATTRI=5
     C                   EVAL      $BRACK=%SCAN(')':$$SVAR:$ATTRI)
     C                   Eval      Msg  = %CHAR($BRACK)
     C                   dsply                   Msg
     *********************************************************************
     C                   EVAL      $ATTRI=2
     C                   EVAL      $BRACK=%SCAN(')':$$SVAR:$ATTRI)
     C                   Eval      Msg  = %CHAR($BRACK)
     C                   dsply                   Msg
     *********************************************************************
     C                   EVAL      $$SVAR='&lt;Colonna'
     C                   EVAL      $BRACK=%SCAN('&lt;':$$SVAR:0)
     C                   Eval      Msg  = %CHAR($BRACK)
     C                   dsply                   Msg
     *********************************************************************
      * Test with start and length arguments
     C                   EVAL      $$SVAR='Dr. Doolittle'
     C                   EVAL      $BRACK=%SCAN('D':$$SVAR:2:4)
     C                   Eval      Msg  = %CHAR($BRACK)
      * Expected 5
     C                   dsply                   Msg
     *********************************************************************
      * Test with start and length arguments
     C                   EVAL      $$SVAR='Dr. Doolittle'
     C                   EVAL      $BRACK=%SCAN('D':$$SVAR:2:3)
     C                   Eval      Msg  = %CHAR($BRACK)
      * Expected 0 because the search starts from 2nd character
      * for three characters:  'r. '
     C                   dsply                   Msg
     *********************************************************************
      * Test with start higher than length of word.
     C                   EVAL      $$SVAR='Dr. Doolittle'
     C                   EVAL      $BRACK=%SCAN('e':$$SVAR:14)
     C                   Eval      Msg  = %CHAR($BRACK)
      * Expected 0 the value of start higher than length of word.
     C                   dsply                   Msg
     *********************************************************************
     C                   SETON                                          LR
