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
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'True'
     C                   ELSE
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'False'
     C                   ENDIF
      * Expected:
      *  £DBG_STR = 'True                                              '
     C     £DBG_Str      DSPLY
     C                   SETON                                          LR