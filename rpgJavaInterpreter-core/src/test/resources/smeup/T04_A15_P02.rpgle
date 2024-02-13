     D name            S             30A
     D pos             S              5U 0
     D £DBG_Str        S             30
     D £DBG_Pas        S              3
      *
     C                   EVAL      £DBG_Pas='P02'
      *
     C                   EVAL      name = 'Amit Jaiswal'
     C                   EVAL      pos = %check('Jais' : name:6)
     C                   EVAL      £DBG_Str='P02_01('+%char(pos)+')'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      name = 'Amit Jaiswal'
     C                   EVAL      pos = %check('A' : name:6)
     C                   EVAL      £DBG_Str='P02_02('+%char(pos)+')'
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR