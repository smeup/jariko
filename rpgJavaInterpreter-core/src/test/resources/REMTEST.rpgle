     D Msg             S             50
     C                   Eval      Msg=%CHAR(%REM(10:3))
     C                   DSPLY                   Msg
     C                   Eval      Msg=%CHAR(%REM(10:-3))
     C                   DSPLY                   Msg
     C                   Eval      Msg=%CHAR(%REM(-10:3))
     C                   DSPLY                   Msg
     C                   Eval      Msg=%CHAR(%REM(-10:-3))
     C                   DSPLY                   Msg
     C                   SETON                                          LR
