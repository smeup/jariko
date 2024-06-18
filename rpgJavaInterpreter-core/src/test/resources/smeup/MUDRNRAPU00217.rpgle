     D £DBG_Str        S             2
     D nRIGAll         S              5I 0
     D nElAgg          S              5I 0
     D SRIG_PTR        S               *   Inz(*Null)
     D SRIG_PTI        S              4  0
     D                 DS                  BASED(SRIG_PTR)
     D SRIG                         300    DIM(5000)
     C                   EVAL      SRIG_PTI = %Alloc(%Size(SRIG) * nRIGAll)
     C                   EVAL      SRIG_PTI =%ReAlloc(SRIG_PTR :
     C                              %Size(SRIG) * (nRIGAll+nElAgg))
     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY