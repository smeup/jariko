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
     C                   SETON                                          LR
