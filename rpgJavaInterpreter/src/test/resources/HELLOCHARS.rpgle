     D MSG             S             50
     D X               S             10
     C                   EVAL      X = 'CIAO'
     C                   IF        %SUBST(X:3:4) = 'AO  '
     C                   EVAL      MSG = 'OK'
     C                   ELSE
     C                   EVAL      MSG = 'NOT OK: ' + %SUBST(X:3:4)
     C                   ENDIF
     C                   DSPLY                   MSG
     C                   SETON                                          LR