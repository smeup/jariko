     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 13/03/20  001693 BMA Creato
     V* 13/03/20  V5R1    BERNI Check-out 001693 in SMEDEV
     V* 28/04/20  001847 BMA Tolta annotation ed eval non necessari e aggiornato timeout
     V*=====================================================================
     D FACTOR2         S             10  0
     D TXT             S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
     D$MSG             S             52
      *---------------------------------------------------------------
      * M A I N
      *---------------------------------------------------------------
      *
     C                   SETOFF                                       343536
     C                   SETOFF                                       373839
     C                   Z-ADD     0             FACTOR2
      * Start time
     C                   TIME                    $TIMST
     C                   DO        10000000
     C   34
     CAN 35
     CANN36
     COR 37
     CAN 38
     CANN39              Z-ADD     1             FACTOR2
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
    MU* TIMEOUT(115)
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
** TXT
Time spent
