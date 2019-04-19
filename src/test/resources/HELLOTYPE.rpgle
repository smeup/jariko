     ** String of 50 chars:
     D Msg             S             50
     ** Indicator:
     D OK              S              1N   INZ(*OFF)
     ** Implicit declaration of a number with 3 digits, 2 of them are decimals
     C                   clear                   X                 3 2
     C                   eval      x = 1.23
     C                   eval      OK=*ON
     C                   IF        OK
     C                   eval      Msg = 'OK is ON X is ' + %CHAR(X)
     C                   else
     C                   eval      Msg = 'OK is OFF X is ' + %CHAR(X)
     C                   endif
     ** Displays "OK is ON X is 1.23"
     C                   dsply                   Msg
     C                   SETON                                          LR