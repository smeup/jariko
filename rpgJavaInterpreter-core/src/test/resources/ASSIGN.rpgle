     D Msg             S             50
     D x               S              6  0 inz(18)
     D y               S              6  0 inz(18)
     D z               S              6  0 inz(18)
     D w               S             12  0 inz(18)
     C                   Eval      x /= 3
     C                   Eval      x /= 3
     C                   Eval      Msg  = 'x is now ' + %char(x)
     C                   dsply                   Msg
     C                   Eval      y *= 3
     C                   Eval      y *= 3
     C                   Eval      Msg  = 'y is now ' + %char(y)
     C                   dsply                   Msg
     C                   Eval      z -= 3
     C                   Eval      z -= 3
     C                   Eval      Msg  = 'z is now ' + %char(z)
     C                   dsply                   Msg
     C                   Eval      w **= 3
     C                   Eval      w **= 3
     C                   Eval      Msg  = 'w is now ' + %char(w)
     C                   dsply                   Msg
     C                   SETON                                          LR
