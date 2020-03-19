     D Msg             S             50
     D i               S              9  0 inz(0)
     C                   Dow       i <= 100000000
     C                   Do        1
     C                   Eval      i = i + 1
     C                   LEAVE
     C                   enddo
     C                   enddo
     C                   Eval      Msg  = 'COUNTER IS NOW ' + %char(i)
     C                   dsply                   Msg
     C                   SETON                                          LR
