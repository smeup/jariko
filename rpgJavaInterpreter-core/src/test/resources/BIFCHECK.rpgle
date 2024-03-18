     D name            S             30A
     D name2           S              4A
     D pos             S              5U 0
     D £DBG_Str        S             30
     D £DBG_Pas        S              3
     D alphabet        C                   'ABCDEFGHIJKLMNOPQRSTUVWXYZ +
     D                                      abcdefghijklmnopqrstuvwxyz +
     D                                      0123456789+/'
      *
     C                   IF        %CHECK(alphabet: 'Antonio Cosentino 123')= 0
     C                   EVAL      £DBG_Str='ok'
     C                   ELSE
     C                   EVAL      £DBG_Str='ko'
     C                   ENDIF
     C     £DBG_Str      DSPLY
