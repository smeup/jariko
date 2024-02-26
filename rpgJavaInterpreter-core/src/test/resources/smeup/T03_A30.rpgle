     D £DBG_Str        S             100          VARYING
      * MOVEL ON/OFF indicatore con valore da variabile
     C                   EVAL      £DBG_Str=' '
     C                   SETON                                        33
     C                   SETOFF                                       34
      *
     C                   Z-ADD     33            $2                5 0
     C                   MOVEL     *OFF          *IN($2)
     C                   Z-ADD     34            $2
     C                   MOVEL     *ON           *IN($2)
     C                   EVAL      £DBG_Str='*IN33='+*IN33+','+
     C                                      '*IN34='+*IN34
     C        £DBG_Str   DSPLY
      * MOVE ON/OFF indicatore con valore da variabile
     C                   EVAL      £DBG_Pas='P02'
     C                   EVAL      £DBG_Str=' '
     C                   SETON                                        33
     C                   SETOFF                                       34
      *
     C                   Z-ADD     33            $2                5 0
     C                   MOVE      *OFF          *IN($2)
     C                   Z-ADD     34            $2
     C                   MOVE      *ON           *IN($2)
     C                   EVAL      £DBG_Str='*IN33='+*IN33+','+
     C                                      '*IN34='+*IN34
     C                   SETON                                          LR