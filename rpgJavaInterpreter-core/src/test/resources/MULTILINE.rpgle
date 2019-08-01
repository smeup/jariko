     D Msg             S             50
     D V1              S              5    INZ(' V1 ')
     D $C              S              3  0 INZ(5)
     C                   EVAL      MSG=%TRIM(V1)+%TRIM(' x ')
     C                               +'.'+%TRIM(%EDITC($C:'Z'))+'_'
     C     MSG           dsply
     C                   SETON                                          LR
