      *====================================================================
      * smeup V6R1.025DV
      * Nome sorgente       : MUTE10_06
      * Sorgente di origine : QTEMP/MUSRC(MUTE10_06)
      * Esportato il        : 20241004 155649
      *====================================================================
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 11/12/19  001362  BERNI  Creato
     V* 11/12/19  V5R1    BMA   Check-out 001362 in SMEUP_TST
     V* 13/12/19  001378  BMA   Tolta annotation
     V* 16/12/19  001378  BMA   Adeguamenti
     V* 17/12/19  V5R1    PEDSTE Check-out 001378 in SMEUP_TST
     V* 23/12/19  V5R1    PEDSTE Check-out 001362 001378 in SMEDEV
     V* 06/09/23  005098  BERNI  Ridenominato partendo da MUTE10_06A per nomenclatura sbagliata
     V* 07/09/23  V6R1    BMA Check-out 005098 in SMEDEV
     V*=====================================================================
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
    RD* Routine test on Array
      *---------------------------------------------------------------------
     C     F_CALL        BEGSR
      *
      * Start Time
     C                   TIME                    $TIMST
      * Loop on PGM
     C                   DO        500
     C                   EVAL      XXRET='1'
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
