      *====================================================================
      * smeup V6R1.025DV
      * Nome sorgente       : MUTE10_05
      * Sorgente di origine : QTEMP/MUSRC(MUTE10_05)
      * Esportato il        : 20241004 155649
      *====================================================================
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 05/12/19  001345  BERNI Creato
     V* 09/12/19  001345  BMA   Alcune modifiche
     V* 09/12/19  V5R1    BMA   Check-out 001345 in SMEUP_TST
     V* 11/12/19  001362  BERNI Aggiunti commenti
     V* 11/12/19  V5R1    BMA   Check-out 001362 in SMEUP_TST
     V* 13/12/19  001378  BMA   Tolta annotation
     V* 16/12/19  001378  BMA   Adeguamenti
     V* 17/12/19  V5R1    PEDSTE Check-out 001378 in SMEUP_TST
     V* 23/12/19  V5R1    PEDSTE Check-out 001345 001362 001378 in SMEDEV
     V* 04/03/21  002673 BMA Tolte annotation NOXMI e variati timeout
     V*  5/03/21  V5R1    BERNIC Check-out 002673 in SMEDEV
     V* 07/09/23  005098  BERNI Rinominato partendo da MUTE10_05A per correggere nomenclatura
     V* 07/09/23  V6R1    BMA Check-out 005098 in SMEDEV
     V*=====================================================================
     V*=====================================================================
     D* OBIETTIVO
     D*  Programma finalizzato ai test performance sulla CALL
     D*---------------------------------------------------------------------
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
    MU* TIMEOUT(0100)
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test SORTA
      *---------------------------------------------------------------------
     C     F_CALL        BEGSR
      * Start Time
     C                   TIME                    $TIMST
      * Variable for loop
     C                   EVAL      $CICL=10000
      * Call
     C                   CALL      'MUTE10_05A'
     C                   PARM                    $CICL
      * End time
     C                   TIME                    $TIMEN
      * Elapsed time
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
      *
     C                   ENDSR
