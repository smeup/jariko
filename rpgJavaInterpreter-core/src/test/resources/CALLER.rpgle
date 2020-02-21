     D Msg             S             50
     D x               S              6  0 inz(18)
     C                   Eval      Msg  = 'Executing CALLER'
     C                   dsply                   Msg
     C                   Eval      Msg  = 'x initialized at: ' + %char(x)
     C                   dsply                   Msg
     C                   Eval      x /= 3
     C                   Eval      Msg  = 'x is now: ' + %char(x)
     C                   dsply                   Msg
     C                   Eval      Msg  = 'Calling CALLED'
     C                   dsply                   Msg
     C                   call      'CALLED'
     C                   Eval      Msg  = 'Calling CALLED'
     C                   dsply                   Msg
     C                   call      'CALLED'
     C                   SETON                                          LR
