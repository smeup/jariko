     D Msg             S             52
     D origin          S             26    INZ('Hello world!')
     D toReplace       S              5    INZ('Pippo')
     C                   Eval      Msg  = %REPLACE(toReplace:origin)
     C     Msg           dsply
     C                   SETON                                          LR
