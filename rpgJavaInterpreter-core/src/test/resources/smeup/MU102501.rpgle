     D £DBG_Str        S             50          VARYING
     C                   MOVEL     '0399999'     AAA003            3
     C                   TESTN                   AAA003               515253
     C                   EVAL      £DBG_Str='51='+%CHAR(*IN51)+
     C                                      ',52='+%CHAR(*IN52)+
     C                                      ',53='+%CHAR(*IN53)
      * 51=1,52=0,53=0
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR