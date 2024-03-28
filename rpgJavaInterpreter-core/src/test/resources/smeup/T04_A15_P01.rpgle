     D name            S             30A
     D name2           S              4A
     D pos             S              5U 0
     D £DBG_Str        S             30
     D £DBG_Pas        S              3
     C                   EVAL      £DBG_Pas='P01'
      *
     C                   EVAL      name = '       Amit Jaiswal'
     C                   EVAL      pos = %check('  ' : name)
     C                   EVAL      £DBG_Str='P01_01('+%char(pos)+')'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      name = '       Amit Jaiswal'
     C                   EVAL      pos = %check(' Atim' : name)
     C                   EVAL      £DBG_Str='P01_02('+%char(pos)+')'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      name = '       Amit Jaiswal'
     C                   EVAL      pos = %check(' Amit Jai' : name)
     C                   EVAL      £DBG_Str='P01_03('+%char(pos)+')'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      name = 'Amit Jaiswal'
     C                   EVAL      pos = %check('A' : name)
     C                   EVAL      £DBG_Str='P01_04('+%char(pos)+')'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      name = 'Amit Jaiswal'
     C                   EVAL      pos = %check('a' : name)
     C                   EVAL      £DBG_Str='P01_05('+%char(pos)+')'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      name = 'Amit Jaiswal'
     C                   EVAL      pos = %check('ab' : name)
     C                   EVAL      £DBG_Str='P01_06('+%char(pos)+')'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      name2 = 'abab'
     C                   EVAL      pos = %check('ab' : name2)
     C                   EVAL      £DBG_Str='P01_07('+%char(pos)+')'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      name2 = 'abab'
     C                   EVAL      pos = %check('ac' : name2)
     C                   EVAL      £DBG_Str='P01_08('+%char(pos)+')'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      name2 = 'abab'
     C                   EVAL      pos = %check('acd' : name2)
     C                   EVAL      £DBG_Str='P01_09('+%char(pos)+')'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      name2 = 'abab'
     C                   EVAL      pos = %check('cd' : name2)
     C                   EVAL      £DBG_Str='P01_10('+%char(pos)+')'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      name2 = 'abab'
     C                   EVAL      pos = %check('ca' : name2)
     C                   EVAL      £DBG_Str='P01_11('+%char(pos)+')'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      name2 = 'abab'
     C                   EVAL      pos = %check('cab' : name2)
     C                   EVAL      £DBG_Str='P01_12('+%char(pos)+')'
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR