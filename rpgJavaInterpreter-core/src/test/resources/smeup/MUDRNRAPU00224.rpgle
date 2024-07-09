     D £DBG_Str        S             2
     D                 DS
     D SMET                         100    DIM(60)
     D  SMET_NUM                     07  0 OVERLAY(SMET:*NEXT)
     D $B              S             06  0
     D $GRU_A          S             02  0
     C                   EVAL      $GRU_A=2
     C                   FOR       $B=1 TO SMET_NUM($GRU_A)
     C                   ENDFOR
     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY