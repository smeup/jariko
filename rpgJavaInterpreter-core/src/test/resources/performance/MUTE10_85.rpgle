   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 05/12/19  001345  BERNI Creato
     V* 09/12/19  001345  BMA   Alcune modifiche
     V* 09/12/19  V5R1    BMA   Check-out 001345 in SMEUP_TST
     V* 11/12/19  001362  BERNI Aggiunti commenti
     V* 11/12/19  V5R1    BMA   Check-out 001362 in SMEUP_TST
     V*=====================================================================
     D* OBIETTIVO
     D*  Programma finalizzato ai test performance sulla CALL
     V*---------------------------------------------------------------------
      * Considerare i seguenti codici operativi
      *+----------+--+---------!--+
      *!RPGLE     !ST!BUILT-IN !ST!
      *+-------------+ --------!--+
      *!CALL      !  !         !  !
      *+----------+--+---------+--+
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
     D $CICL           S              7  0
      * Main
     C                   EXSR      F_CALL
      *
    MU* TIMEOUT(7100)
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test SORTA
      *---------------------------------------------------------------------
     C     F_CALL        BEGSR
      * Start time
     C                   TIME                    $TIMST
      * Variable for loop
     C                   EVAL      $CICL=1000000
      * Call
     C                   CALL      'MUTE10_05'
     C                   PARM                    $CICL
      * end time
     C                   TIME                    $TIMEN
      * Elapsed time
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
      *
     C                   ENDSR
