     D Msg             S             50    varying
     D V               S             10    varying
     C                   eval      V = 'Ciao'
     ***** The following line generates a runtime error: RNQ0100
     C                   IF        %SUBST(V:5:2) = '  '
     C                   EVAL      Msg = Msg + ' V padded'
     C                   else
     C                   eval      Msg = Msg + ' V not padded'
     C                   endif
     C                   dsply                   Msg
     C                   SETON                                          LR
