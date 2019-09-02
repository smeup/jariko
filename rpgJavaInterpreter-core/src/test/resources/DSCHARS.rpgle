     D Msg             S             50
     DA01              DS             3
     DA02              DS             3
     D Res             S              6
     C                   Eval      A01 = 'X 1'
     C                   Eval      A02 = 'Y 2'
     C                   Eval      Res = A01 + A02
     C                   Eval      Msg  = 'Result is: ' + Res
     C                   dsply                   Msg
     C                   SETON                                          LR
