     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 15/02/23  004649  BUSFIO Creazione
     V* 16/02/23  004649  BUSFIO Aggiunta notazione per mute di performance
     V*=====================================================================
     D*  OBIETTIVO
     D*  Program finalized to test performance of opcode OCCUR
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
     I/COPY QILEGEN,£TABB£1DS
     I/COPY QILEGEN,£PDS
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * Start time
    MU* TIMEOUT(10)
     C                   TIME                    $TIMST
     C                   EVAL      $X = 0
      * Test Occur
     C     1             DO        100000        $X
     C     $X            OCCUR     $ADS
     C                   EVAL      $COD = $X
     C                   EVAL      $DES = %EDITC($X:'Z')
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
      /COPY QILEGEN,£INZSR
      *---------------------------------------------------------------
     C     £INIZI        BEGSR
     C                   ENDSR
      *---------------------------------------------------------------
