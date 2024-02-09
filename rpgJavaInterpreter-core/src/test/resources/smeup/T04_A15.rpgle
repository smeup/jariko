     D name            S             30A
     D name2           S              4A
     D pos             S              5U 0
     D £DBG_Str        S             30
     D £DBG_Pas        S              3
     D alphabet        C                   'ABCDEFGHIJKLMNOPQRSTUVWXYZ +
     D                                      abcdefghijklmnopqrstuvwxyz +
     D                                      0123456789+/'
      *
     C                   EXSR      SEZ_T04_A15
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
    RD* Errori programma MULANGT04 sezione A15
      *--------------------------------------------------------------*
     C     SEZ_T04_A15   BEGSR
    OA* A&.BIFN(%CHECK)
     D* %CHECK semplice
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
    OA* A&.BIFN(%CHECK)
     D* %CHECK con posizione
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
      *
    OA* A&.BIFN(%CHECK)
     D* %CHECK ricerca in alfabeto
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
     C                   ENDSR