     D Msg             S             50
     D Ar              S              1    DIM(3)
     D Br              S              2    DIM(3)
     D Cr              S              4    DIM(3)
     D Str             S              3    INZ('ABC')
     C                   Eval      Ar = Str
     C                   Eval      Msg = Ar(1) + '-' + Ar(2) + '-' + Ar(3)
     C     Msg           dsply
     C                   Eval      Br = Str
     C                   Eval      Msg = Br(1) + '-' + Br(2) + '-' + Br(3)
     C     Msg           dsply
      * Questa istruzione è equivalente alle 3 precedenti
     C                   Eval      Cr = Str
     C                   Eval      Msg = Cr(1) + '-' + Cr(2) + '-' + Cr(3) + '-'
     C     Msg           dsply
     C                   SETON                                          LR
