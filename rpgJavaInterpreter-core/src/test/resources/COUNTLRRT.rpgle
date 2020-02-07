     D Flag            S              2
     D Msg             S             12
     D n               S              8  0
     C     *entry        plist
     C                   parm                    Flag
     C                   Eval      N = n + 1
     C                   Eval      Msg  = 'Counter: ' + %char(n)
     C     Msg           dsply
     C                   IF        Flag='LR'
     C                   SETON                                          LR
     C                   ELSE
     C                   SETON                                          RT
     C                   ENDIF

