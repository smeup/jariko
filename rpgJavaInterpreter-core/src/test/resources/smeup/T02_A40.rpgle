     D £DBG_Str        S             50
     D                 DS
     D A40_DS4                      100    DIM(5) DESCEND
     D  DS4_FL1                       9    OVERLAY(A40_DS4:1)
     D  DS4_FL2                      10    OVERLAY(A40_DS4:*NEXT)
      *
     C                   EVAL      A40_DS4(1) = 'AAAAAZZZZZ'
     C                   EVAL      A40_DS4(2) = 'NNNNNFFFFF'
     C                   EVAL      A40_DS4(3) = 'MMMMMGGGGG'
     C                   SORTA     DS4_FL1
     C                   EVAL      £DBG_Str='DS4_FL1('+%TRIMR(A40_DS4(1))
     C                                                +%TRIMR(A40_DS4(2))
     C                                                +%TRIMR(A40_DS4(3))+')'
      * Expect 'DS4_FL1(NNNNNFFFFFMMMMMGGGGGAAAAAZZZZZ)'
     C     £DBG_Str      DSPLY
      *
     C                   SORTA     DS4_FL2
     C                   EVAL      £DBG_Str='DS4_FL2('+%TRIMR(A40_DS4(1))
     C                                                +%TRIMR(A40_DS4(2))
     C                                                +%TRIMR(A40_DS4(3))+')'
      * Expect 'DS4_FL2(AAAAAZZZZZMMMMMGGGGGNNNNNFFFFF)'
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR