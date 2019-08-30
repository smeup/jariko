     D Msg             S             50
     D                 DS
     DA01                             3    DIM(2)
     D A01_A                          1    OVERLAY(A01:1)
     D A01_N                          2  0 OVERLAY(A01:*NEXT)
     D Res             S              2  0
     C                   Eval      A01(1) = 'X 1'
     C                   Eval      A01(2) = 'Y 2'
     C                   Eval      Res = A01_N(1) + A01_N(2)
     C                   Eval      Msg  = 'Result is: ' + %CHAR(Res)
     C                   dsply                   Msg
     C                   SETON                                          LR
