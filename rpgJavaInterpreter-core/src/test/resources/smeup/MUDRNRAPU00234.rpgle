     D £DBG_Str        S             2

     D $A              S              5  0
     D £43A            S            256    DIM(256)
     D $CPA            S              5  0

     C                   EVAL      $CPA=1
     C                   EVAL      $A=%SCAN('(':£43A($CPA))

     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY