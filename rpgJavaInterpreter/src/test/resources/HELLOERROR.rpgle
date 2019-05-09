     D Msg             S             50
     ** Implicit declaration of a number with 3 digits, 2 of them are decimals
     C                   clear                   X                 3 2
     ** The following row generates a runtime error
     C                   eval      x = 123
     C                   eval      Msg = 'X is ' + %CHAR(X)
     C                   dsply                   Msg
     C                   SETON                                          LR
