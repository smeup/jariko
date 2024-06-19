     D £DBG_Str        S             2
     D SRIG_PTR        S               *   Inz(*Null)
     D                 DS                  BASED(SRIG_PTR)
     D SRIG                         300    DIM(5000)
     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY