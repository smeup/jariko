     D Msg             S             50
     D TXT             S            100    DIM(10) PERRCD(1) CTDATA
     D $N1             S              5  0
     C                   EVAL      $N1=%SCAN('e':TXT(1))
     C                   EVAL      Msg=%CHAR($N1)
     C     Msg           dsply
     C                   SETON                                        LR
** TXT
Time spent




TEST
--------------
xxxxxxxxxxxxx
yyyyyyyyyyyyyy
zzzzzzzzzzz
