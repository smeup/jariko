     D Msg             S             52
     D origin          S             26    INZ('Hello world!')
     D toRepl          S              5    INZ('Pippo')
     C                   Eval      Msg  = %REPLACE(toRepl:origin)
     C     Msg           dsply
     *******************************************************************
     C                   Eval      Msg  = %REPLACE(toRepl:origin:7)
     C     Msg           dsply
     *******************************************************************
     C                   Eval      Msg  = %REPLACE(toRepl:origin:7:1)
     C     Msg           dsply
     *******************************************************************
     C                   Eval      Msg  = %REPLACE(toRepl:origin:7:3)
     C     Msg           dsply
     *******************************************************************
     C                   Eval      Msg  = %REPLACE(toRepl:origin:7:0)
     C     Msg           dsply
     *******************************************************************
     C                   SETON                                        LR
