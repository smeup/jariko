     D Msg             S             50
     D X               S             10
     C                   eval      X = '----------'
     C                   eval      X = 'Ciao'
     C                   IF        %SUBST(X:5:2) = '  '
     C                   EVAL      Msg = 'X padded'
     C                   else
     C                   eval      Msg = 'X not padded'
     C                   endif
     C                   dsply                   Msg
     C                   SETON                                          LR
