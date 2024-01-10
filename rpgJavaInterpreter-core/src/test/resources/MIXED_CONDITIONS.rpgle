     D A02_N20A        S              2  0 INZ(10)
     D A02_N20B        S              2  0 INZ(10)
     D A02_A10C        S             10    INZ('True')
     D A02_A10D        S             10    INZ('True')
     D A02_N1_E        S              1N   INZ(*ON)
     D A02_N1_F        S              1N   INZ(*ON)
     D A02_N20G        S              2  0 INZ(10)
     D A02_N20H        S              2  0 INZ(10)
      *
     D £DBG_Str        S            100
      *
     C     1             IFEQ      1
     C     A02_N20A      ANDEQ     A02_N20B
     C     A02_A10C      ANDNE     A02_A10D
     C     A02_N1_E      OREQ      A02_N1_F
     C     A02_N20G      ORNE      A02_N20H
     C                   EVAL      £DBG_Str='IF1 = True'
     C                   ELSE
     C                   EVAL      £DBG_Str='IF1 = False'
     C                   ENDIF
      * Expected:
      *  £DBG_STR = 'IF1 = True'
     C     £DBG_Str      DSPLY
      *******
     C     1             IFEQ      1
     C     A02_N20A      ANDEQ     A02_N20B
     C     A02_A10C      ANDEQ     A02_A10D
     C     A02_N1_E      OREQ      A02_N1_F
     C     A02_N20G      ORNE      A02_N20H
     C                   EVAL      £DBG_Str='IF2 = True'
     C                   ELSE
     C                   EVAL      £DBG_Str='IF2 = False'
     C                   ENDIF
      *
      * Expected:
      *  £DBG_STR = 'IF2 = True'
     C     £DBG_Str      DSPLY
      *******
     C     1             IFEQ      2
     C     A02_N20A      ANDEQ     A02_N20B
     C     A02_A10C      ANDEQ     A02_A10D
     C     A02_N1_E      OREQ      A02_N1_F
     C     A02_N20G      ORNE      A02_N20H
     C                   EVAL      £DBG_Str='IF3 = True'
     C                   ELSE
     C                   EVAL      £DBG_Str='IF3 = False'
     C                   ENDIF
      *
      * Expected:
      *  £DBG_STR = 'IF3 = True'
     C     £DBG_Str      DSPLY
      *******
     C     1             IFEQ      2
     C     A02_N20A      ANDNE     A02_N20B
     C     A02_A10C      ANDNE     A02_A10D
     C     A02_N1_E      OREQ      A02_N1_F
     C     A02_N20G      ORNE      A02_N20H
     C                   EVAL      £DBG_Str='IF4 = True'
     C                   ELSE
     C                   EVAL      £DBG_Str='IF4 = False'
     C                   ENDIF
      *
      * Expected:
      *  £DBG_STR = 'IF4 = True'
     C     £DBG_Str      DSPLY
      *******
     C     1             IFEQ      2
     C     A02_N20A      ANDNE     A02_N20B
     C     A02_A10C      ANDNE     A02_A10D
     C     A02_N1_E      ORNE      A02_N1_F
     C     A02_N20G      ORNE      A02_N20H
     C                   EVAL      £DBG_Str='IF5 = True'
     C                   ELSE
     C                   EVAL      £DBG_Str='IF5 = False'
     C                   ENDIF
      *
      * Expected:
      *  £DBG_STR = 'IF5 = False'
     C     £DBG_Str      DSPLY
      *******
     C     1             IFNE      1
     C     A02_N20A      ANDNE     A02_N20B
     C     A02_A10C      ANDNE     A02_A10D
     C     A02_N1_E      ORNE      A02_N1_F
     C     A02_N20G      ORNE      A02_N20H
     C                   EVAL      £DBG_Str='IF6 = True'
     C                   ELSE
     C                   EVAL      £DBG_Str='IF6 = False'
     C                   ENDIF
      *
      * Expected:
      *  £DBG_STR = 'IF6 = False'
     C     £DBG_Str      DSPLY
      *******
     C     1             IFEQ      1
     C     A02_N20A      ANDEQ     A02_N20B
     C     A02_A10C      ANDNE     A02_A10D
     C     A02_N1_E      OREQ      A02_N1_F
     C     2             ANDNE     2
     C     A02_N20G      ORNE      A02_N20H
     C     3             ANDNE     3
     C                   EVAL      £DBG_Str='IF7 = True'
     C                   ELSE
     C                   EVAL      £DBG_Str='IF7 = False'
     C                   ENDIF
      * Expected:
      *  £DBG_STR = 'IF7 = False'
     C     £DBG_Str      DSPLY
      *******
     C     1             IFEQ      1
     C     A02_N20A      ANDEQ     A02_N20B
     C     A02_A10C      ANDNE     A02_A10D
     C     A02_N1_E      OREQ      A02_N1_F
     C     A02_N20G      ORNE      A02_N20H
     C     3             ANDNE     3
     C                   EVAL      £DBG_Str='IF8 = True'
     C                   ELSE
     C                   EVAL      £DBG_Str='IF8 = False'
     C                   ENDIF
      * Expected:
      *  £DBG_STR = 'IF8 = True'
     C     £DBG_Str      DSPLY
      *******
     C                   SETON                                          LR