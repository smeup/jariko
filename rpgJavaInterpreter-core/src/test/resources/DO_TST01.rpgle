     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 18/03/20  V5R1   FP  Creato xxxxx
     V*=====================================================================
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
      *---------------------------------------------------------------
     D $MSG            S             52
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * Start time
     C                   TIME                    $TIMST
      *
     C                   DO        10000000
      *
     C                   ENDDO
      * End time
     C                   TIME                    $TIMEN
      * Elapsed time
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
      * Display message
     C                   EVAL      $MSG='DO_TST01(91ms) Spent ' +               COSTANTE
     C                             %TRIM(%EDITC($TIMMS:'Q'))+'ms '              COSTANTE
     C     $MSG          DSPLY     £PDSSU
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
