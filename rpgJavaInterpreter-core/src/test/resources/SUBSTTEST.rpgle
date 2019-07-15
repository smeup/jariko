     D Msg             S             12
     D $$SVAR          S             20    INZ('xxx)yyz')
     D $ATTRI          S              2  0
     D $BRACK          S              5  0
     C                   EVAL      $ATTRI=2
     C                   EVAL      $BRACK=5
     C                   EVAL      Msg  =%SUBST($$SVAR:$ATTRI+1:$BRACK-1)
     C                   dsply                   Msg
     *********************************************************************
     C                   SETON                                          LR
