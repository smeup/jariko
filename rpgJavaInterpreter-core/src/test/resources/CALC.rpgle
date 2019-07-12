     D Msg             S             50
     D x               S              6  0 inz(1)
     D y               S              6  0 inz(2)
     D z               S              6  0 inz(3)
     C                   Eval      x=y*3
     C                   Eval      z=y/3
     C                   Eval      Msg  = 'x is now ' + %char(x)
     C                   dsply                   Msg
     C                   Eval      Msg  = 'y is now ' + %char(y)
     C                   dsply                   Msg
     C                   Eval      Msg  = 'z is now ' + %char(z)
     C                   dsply                   Msg
     C                   SETON                                          LR
