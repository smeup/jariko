     D A70_A03         S              3
     D A70_A04         S              4
     D A70_A05         S              5
     D A70_A06         S              6
     D A70_A07         S              7
     D A70_A09         S              9
     D A70_A10         S             10
     D A70_N1          S              1  0 INZ(0)
     D £DBG_Str        S             52
      * CAT
     C                   MOVE      'MR.'         A70_A03
     C                   MOVE      ' SMITH'      A70_A06
     C     A70_A03       CAT       A70_A06       A70_A09
     C                   EVAL      £DBG_Str='CAT_1('+A70_A09+')'
      *
     C                   MOVE      '/400'        A70_A04
     C     'RPG'         CAT       A70_A04       A70_A07
     C                   EVAL      £DBG_Str=%TRIMR(£DBG_Str)
     C                                     +' CAT_2('+A70_A07+')'
      *
     C                   CLEAR                   A70_A04
     C                   CLEAR                   A70_A05
      *
     C                   MOVE      '/400'        A70_A04
     C     'RPG'         CAT       A70_A04       A70_A05
     C                   EVAL      £DBG_Str=%TRIMR(£DBG_Str)
     C                                     +' CAT_3('+A70_A05+')'
      * Expect 'CAT_1(MR. SMITH) CAT_2(RPG/400) CAT_3(RPG/4)'
     C     £DBG_Str      DSPLY
      *
     D* CAT blank number
     C                   CLEAR                   A70_A09
     C                   MOVEL(P)  'ABC'         A70_A09
     C                   MOVE      'XYZ'         A70_A03
     C                   CAT       A70_A03:2     A70_A09
     C                   EVAL      £DBG_Str='CAT_1('+A70_A09+')'
      *
     C                   MOVE      'Mr.   '      A70_A06
     C                   MOVE      'Smith  '     A70_A07
     C     A70_A06       CAT       A70_A07:1     A70_A09
     C                   EVAL      £DBG_Str=%TRIMR(£DBG_Str)
     C                                     +' CAT_2('+A70_A09+')'
      * Expect 'CAT_1(ABC  XYZ )CAT_2(Mr. Smith)'
     C     £DBG_Str      DSPLY
      *
      * CAT(P)
     C                   MOVE      *ALL'*'       A70_A10
     C                   MOVE      '/400'        A70_A04
     C     'RPG'         CAT(P)    A70_A04       A70_A10
     C                   EVAL      £DBG_Str='CAT_1('+A70_A10+')'
      * Expect 'CAT_1(RPG/400   )'
     C     £DBG_Str      DSPLY
      *
     D* CAT(P) with blank number
     C                   MOVE      'RPG '        A70_A04
     C                   MOVE      'IV    '      A70_A06
     C     A70_A04       CAT(P)    A70_A06:A70_N1A70_A10
     C                   EVAL      £DBG_Str='CAT_1('+A70_A10+')'
      * Expect 'CAT_1(RPGIV     )'
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR