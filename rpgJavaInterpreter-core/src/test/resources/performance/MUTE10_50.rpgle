     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 13/03/20  001693 BMA Creato
     V*=====================================================================
     D TXT             S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
     D$MSG             S             52
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
     C                   CLEAR                   FACTOR1           6
     C                   CLEAR                   FACTOR2          10
     C                   CLEAR                   RESULT           20
     C                   MOVEL(P)  'HELLO '      FACTOR1
     C                   MOVEL(P)  'WORLD!    '  FACTOR2
     C                   MOVEL(P)  *ALL'X'       RESULT
      * Start time
     C                   TIME                    $TIMST
     C                   DO        10000000
     C     FACTOR1       CAT       FACTOR2       RESULT
     C                   ENDDO
     C     RESULT        DSPLY     £PDSSU
      * End time
     C                   TIME                    $TIMEN
      * Elapsed time
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
      * Display message
     C                   EVAL      $MSG=%trim(TXT(1))+' '+
     C                             %TRIM(%EDITC($TIMMS:'Q'))+'ms'
     C     $MSG          DSPLY     £PDSSU
    MU* TIMEOUT(150)
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
** TXT
Time spent
