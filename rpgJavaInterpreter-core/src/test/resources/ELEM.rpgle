     D arr1d           S             20    DIM(10)
     D table           S             10    DIM(20) ctdata
     D mds             DS            20    occurs(30)
     D msg             S             50
     C                   Eval      MSG  = %CHAR(%elem(arr1d))
     C     Msg           dsply
     C                   Eval      MSG  = %CHAR(%elem(table))
     C     Msg           dsply
     C                   Eval      MSG  = %CHAR(%elem(mds))
     C     Msg           dsply
     C                   SETON                                          LR
