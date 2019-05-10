     D Msg             S             50    varying
     C                   IF        'C ' = 'C'
     C                   EVAL      Msg = 'Cb is equal to C'
     C                   else
     C                   eval      Msg = 'Cb is not equal to C'
     C                   endif
     C                   IF        'C ' <> 'C'
     C                   EVAL      Msg = Msg + ' and Cb differs from C'
     C                   else
     C                   eval      Msg = Msg + ' and Cb does not differ from C'
     C                   endif
     C                   dsply                   Msg
     C                   SETON                                          LR
