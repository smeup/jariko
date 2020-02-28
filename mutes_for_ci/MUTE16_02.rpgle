     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 27/02/20  V5R1    FP Creato
     V*=====================================================================
     D  NUM_FACTOR1    S              2  0
     D  NUM_FACTOR2    S              2  0
     D  STR_FACTOR1    S              2
     D  STR_FACTOR2    S              2
     D  STR_FACTOR1_V  S             10    VARYING
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * Compare homogeneous types (number)
     C                   SETOFF                                       202122
     C                   Z-ADD     5             NUM_FACTOR1
     C                   Z-ADD     10            NUM_FACTOR2
    MU* VAL1(*IN20) VAL2('FALSE') COMP(EQ)
     C     NUM_FACTOR1   COMP      NUM_FACTOR2                        202122
    MU* VAL1(*IN21) VAL2('TRUE') COMP(EQ)
     C     NUM_FACTOR1   COMP      NUM_FACTOR2                        202122
    MU* VAL1(*IN22) VAL2('FALSE') COMP(EQ)
     C     NUM_FACTOR1   COMP      NUM_FACTOR2                        202122
      *
     C                   SETOFF                                       202122
     C                   Z-ADD     10            NUM_FACTOR1
     C                   Z-ADD     10            NUM_FACTOR2
    MU* VAL1(*IN20) VAL2('FALSE') COMP(EQ)
     C     NUM_FACTOR1   COMP      NUM_FACTOR2                        202122
    MU* VAL1(*IN21) VAL2('FALSE') COMP(EQ)
     C     NUM_FACTOR1   COMP      NUM_FACTOR2                        202122
    MU* VAL1(*IN22) VAL2('TRUE') COMP(EQ)
     C     NUM_FACTOR1   COMP      NUM_FACTOR2                        202122
      *
     C                   SETOFF                                       202122
     C                   Z-ADD     10            NUM_FACTOR1
     C                   Z-ADD     5             NUM_FACTOR2
    MU* VAL1(*IN20) VAL2('TRUE') COMP(EQ)
     C     NUM_FACTOR1   COMP      NUM_FACTOR2                        202122
    MU* VAL1(*IN21) VAL2('FALSE') COMP(EQ)
     C     NUM_FACTOR1   COMP      NUM_FACTOR2                        202122
    MU* VAL1(*IN22) VAL2('FALSE') COMP(EQ)
     C     NUM_FACTOR1   COMP      NUM_FACTOR2                        202122
      *
      * Compare homogeneous types (string)
     C                   SETOFF                                       202122
     C                   MOVEL(P)  '5'           STR_FACTOR1
     C                   MOVEL(P)  '10'          STR_FACTOR2
    MU* VAL1(*IN20) VAL2('TRUE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
    MU* VAL1(*IN21) VAL2('FALSE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
    MU* VAL1(*IN22) VAL2('FALSE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
      *
     C                   SETOFF                                       202122
     C                   MOVEL(P)  '10'          STR_FACTOR1
     C                   MOVEL(P)  '10'          STR_FACTOR2
    MU* VAL1(*IN20) VAL2('FALSE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
    MU* VAL1(*IN21) VAL2('FALSE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
    MU* VAL1(*IN22) VAL2('TRUE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
      *
     C                   SETOFF                                       202122
     C                   MOVEL(P)  '10'          STR_FACTOR1
     C                   MOVEL(P)  '5'           STR_FACTOR2
    MU* VAL1(*IN20) VAL2('FALSE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
    MU* VAL1(*IN21) VAL2('TRUE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
    MU* VAL1(*IN22) VAL2('FALSE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
      *
     C                   SETOFF                                       202122
     C                   MOVEL(P)  'A '          STR_FACTOR1
     C                   MOVEL(P)  ' A'          STR_FACTOR2
    MU* VAL1(*IN20) VAL2('TRUE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
    MU* VAL1(*IN21) VAL2('FALSE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
    MU* VAL1(*IN22) VAL2('FALSE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
      *
     C                   SETOFF                                       202122
     C                   MOVEL(P)  'A'           STR_FACTOR1
     C                   MOVEL(P)  'Z'           STR_FACTOR2
    MU* VAL1(*IN20) VAL2('FALSE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
    MU* VAL1(*IN21) VAL2('TRUE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
    MU* VAL1(*IN22) VAL2('FALSE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
      *
     C                   SETOFF                                       202122
     C                   MOVEL(P)  '0'           STR_FACTOR1
     C                   MOVEL(P)  'A'           STR_FACTOR2
    MU* VAL1(*IN20) VAL2('TRUE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
    MU* VAL1(*IN21) VAL2('FALSE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
    MU* VAL1(*IN22) VAL2('FALSE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
      *
     C                   SETOFF                                       202122
     C                   MOVEL(P)  '0'           STR_FACTOR1
     C                   MOVEL(P)  '9'           STR_FACTOR2
    MU* VAL1(*IN20) VAL2('FALSE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
    MU* VAL1(*IN21) VAL2('TRUE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
    MU* VAL1(*IN22) VAL2('FALSE') COMP(EQ)
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
      *
     C                   SETOFF                                       202122
     C                   EVAL      STR_FACTOR1_V='0'
     C                   MOVEL(P)  '9'           STR_FACTOR2
    MU* VAL1(*IN20) VAL2('FALSE') COMP(EQ)
     C     STR_FACTOR1_V COMP      STR_FACTOR2                        202122
    MU* VAL1(*IN21) VAL2('TRUE') COMP(EQ)
     C     STR_FACTOR1_V COMP      STR_FACTOR2                        202122
    MU* VAL1(*IN22) VAL2('FALSE') COMP(EQ)
     C     STR_FACTOR1_V COMP      STR_FACTOR2                        202122
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------