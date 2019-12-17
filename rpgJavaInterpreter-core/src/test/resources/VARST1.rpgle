     D VarStr          S             50    Varying    Inz
     D VarStr2         S             50    Varying    Inz
     D StdStr          S             50    Inz
     **-------------------------------------------------------------------
     C                   Eval      StdStr = %TrimR(StdStr) + 'A'
     C     StdStr        DSPLY
     **-------------------------------------------------------------------
     C                   Eval      StdStr = StdStr + 'A'
     C     StdStr        DSPLY
     **-------------------------------------------------------------------
     C                   Eval      VarStr = %TrimR(VarStr) + 'A'
     C     VarStr        DSPLY
     **-------------------------------------------------------------------
     C                   Eval      VarStr = VarStr + 'A'
     C     VarStr        DSPLY
     **-------------------------------------------------------------------
     C                   Eval      VarStr2 = VarStr2 + 'A'
     C     VarStr2       DSPLY
     **-------------------------------------------------------------------
     C                   SETON                                          LR
