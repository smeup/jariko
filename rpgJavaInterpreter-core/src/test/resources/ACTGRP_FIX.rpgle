     H ACTGRP('MYACT')
     D X               S              1  0
     D Msg             S             12
     D UnlMsg          S               0
     C                   EVAL      X = X + 1
     C                   EVAL      Msg = %CHAR(X)
     C     msg           dsply
     C                   SETON                                          RT
