      *====================================================================
      * smeup V6R1.025DV
      * Nome sorgente       : MUTE10_82
      * Sorgente di origine : QTEMP/MUSRC(MUTE10_82)
      * Esportato il        : 20241004 155649
      *====================================================================
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 06/09/23  005098  BERNI Creato a partire dal MUTE10_06B
     V* 07/09/23  V6R1    BMA Check-out 005098 in SMEDEV
     V*=====================================================================
     D*  OBIETTIVO
     D*  Pgm testing performance with big array
     V*---------------------------------------------------------------------
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
     D ARRAY           S          10000    DIM(500)
     D TXT             S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
     D$MSG             S             52
     D XXRET           S              1
      *
      * Main
     C                   EXSR      F_CALL
      *
    MU* TIMEOUT(40000)
      *
     C                   SETON                                        LR
      *
      *---------------------------------------------------------------------
    RD* Routine test Array
      *---------------------------------------------------------------------
     C     F_CALL        BEGSR
      *
      * Start Time
     C                   TIME                    $TIMST
      * Loop on PGM
     C                   DO        500
     C                   EVAL      XXRET=' '
     C                   CALL      'MUTE10_06A'
     C                   PARM                    ARRAY
     C                   PARM                    XXRET
     C
     C                   ENDDO
      * End Time
     C                   TIME                    $TIMEN
      * Elapsed Time
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
      *
     C                   EVAL      $TIMMS=$TIMMS/1000
      *
      * Display Message with elapsed time
     C                   EVAL      $MSG=%trim(TXT(1))+' '+
     C                             %TRIM(%EDITC($TIMMS:'Q'))+'ms'
     C     $MSG          DSPLY     £PDSSU
      *
     C                   ENDSR
** TXT
Time spent
