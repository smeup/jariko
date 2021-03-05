     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 20/04/20  001835 BMA Creato
     V*=====================================================================
     D TXT             S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
     D $X              S             10I 0
     D$MSG             S             52
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * Start time
     C                   TIME                    $TIMST
     C                   FOR       $X=1 TO 10000000
     C                   ENDFOR
      * End time
     C                   TIME                    $TIMEN
      * Elapsed time
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
      * Display message
     C                   EVAL      $MSG=%trim(TXT(1))+' '+
     C                             %TRIM(%EDITC($TIMMS:'Q'))+'ms'
     C     $MSG          DSPLY     Â£PDSSU
    MU* TIMEOUT(310)
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
** TXT
Time spent
