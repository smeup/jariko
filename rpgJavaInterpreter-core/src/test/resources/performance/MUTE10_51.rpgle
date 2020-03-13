     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 13/03/20  001693 BMA Creato
     V*=====================================================================
     D  NUM_FACTOR1    S              2  0
     D  NUM_FACTOR2    S              2  0
     D  STR_FACTOR1    S              2
     D  STR_FACTOR2    S              2
     D TXT             S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
     D$MSG             S             52
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * Start time
     C                   TIME                    $TIMST
      * Compare homogeneous types (number)
     C                   SETOFF                                       202122
     C                   Z-ADD     5             NUM_FACTOR1
     C                   Z-ADD     10            NUM_FACTOR2
     C                   DO        10000000
     C     NUM_FACTOR1   COMP      NUM_FACTOR2                        202122
     C                   ENDDO
      * Compare homogeneous types (string)
     C                   SETOFF                                       202122
     C                   MOVEL(P)  '5'           STR_FACTOR1
     C                   MOVEL(P)  '10'          STR_FACTOR2
     C                   DO        10000000
     C     STR_FACTOR1   COMP      STR_FACTOR2                        202122
     C                   ENDDO
      * End time
     C                   TIME                    $TIMEN
      * Elapsed time
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
      * Display message
     C                   EVAL      $MSG=%trim(TXT(1))+' '+
     C                             %TRIM(%EDITC($TIMMS:'Q'))+'ms'
     C     $MSG          DSPLY     £PDSSU
    MU* TIMEOUT(1000)
     C                   SETON                                        LR
      *---------------------------------------------------------------
** TXT
Time spent
