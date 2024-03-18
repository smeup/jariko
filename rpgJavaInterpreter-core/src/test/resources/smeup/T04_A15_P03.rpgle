     D name            S             30A
     D pos             S              5U 0
     D £DBG_Str        S             30
     D £DBG_Pas        S              3
     D alphabet        C                   'ABCDEFGHIJKLMNOPQRSTUVWXYZ +
     D                                      abcdefghijklmnopqrstuvwxyz +
     D                                      0123456789+/'
      *
     C                   EVAL      £DBG_Pas='P03'
      *
     C                   EVAL      name = 'Amit Jaiswal'
     C                   EVAL      pos = %check(alphabet: name)
     C                   EVAL      £DBG_Str='P03_01('+%char(pos)+')'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      name = '££ t abc1234'
     C                   EVAL      pos = %check(alphabet: name)
     C                   EVAL      £DBG_Str='P03_02('+%char(pos)+')'
     C     £DBG_Str      DSPLY
      *
     C                   IF        %CHECK(alphabet: 'Antonio Cosentino')= 0
     C                   EVAL      £DBG_Str='P03_03(ok)'
     C                   ELSE
     C                   EVAL      £DBG_Str='P03_03(ko)'
     C                   ENDIF
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR