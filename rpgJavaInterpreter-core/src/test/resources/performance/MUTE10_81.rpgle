     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 17/02/23  004649  BUSFIO Creazione
     V*=====================================================================
     D*  OBIETTIVO
     D*  Program finalized to test performance of opcode OCCUR no sequential
     D* without DS eval
     D*
     V*=====================================================================
     D $X              S              7  0 INZ
      * DS
     D $ADS            DS                  OCCURS(100000)
     D  $COD                          7  0
     D  $DES                         20
      * Time
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
     D $MSG            S             52
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * Start time
    MU* TIMEOUT(10)
     C                   EVAL      $X = 1
     C                   TIME                    $TIMST
      * Test Occur
     C     1             DO        100000        $X
     C     $X            OCCUR     $ADS
     C                   EVAL      $X = $X + 15
     C                   ENDDO
      * End time
     C                   TIME                    $TIMEN
      * Elapsed time
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
      * Display message
     C                   EVAL      $MSG='Time spent '+                          COSTANTE
     C                             %TRIM(%EDITC($TIMMS:'Q'))+'ms'
     C     $MSG          DSPLY     £PDSSU
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
     C     £INIZI        BEGSR
     C                   ENDSR
      *---------------------------------------------------------------
