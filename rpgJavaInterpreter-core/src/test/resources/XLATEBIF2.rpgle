     D Msg             S             26
     D Original        S             26    INZ('http://xxx.smeup.com      ')
     D a               S             27    INZ('Key                       w')
     D b               S             26    INZ('Value                    u')
     C                   Eval      Msg  = %XLATE(a:b:Original)
     C     Msg           dsply
     C                   SETON                                          LR
