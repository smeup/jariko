     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 17/06/19  000908  CM Creato
     V* 19/06/19  V5R1    BMA Check-out 000908 in SMEDEV
     V* 28/10/19  001213 BMA Aggiunta annotation TIMEOUT
     V* 29/10/19  V5R1    BERNI Check-out 001213 in SMEDEV
     V* 04/05/20  001855 BMA Tolte annotation di valutazione (non sensate in pgm performance)
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
     D$MSG             S             52
     D TXT             S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
     C                   EXSR      F_CALL
      *
    MU* TIMEOUT(7500)
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test SORTA
      *---------------------------------------------------------------------
     C     F_CALL        BEGSR
     C                   TIME                    $TIMST
     C                   DO        1000
     C                   CALL      'MUTE10_01A'
     C                   ENDDO
     C                   TIME                    $TIMEN
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
      * Display message
     C                   EVAL      $MSG=%trim(TXT(1))+' '+
     C                             %TRIM(%EDITC($TIMMS:'Q'))+'ms'
     C     $MSG          DSPLY     £PDSSU
     C                   ENDSR
** TXT
Time spent
