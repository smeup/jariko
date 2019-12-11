     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 11/12/19  001362  BERNI Creato
     V* 11/12/19  V5R1    BMA   Check-out 001362 in SMEUP_TST
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
    MU* Type="NOXMI"
    MU* TIMEOUT(75000)
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
     C                   CALL      'MUTE10_06'
     C                   PARM                    ARRAY
     C                   PARM                    XXRET
     C
     C                   ENDDO
      * End Time
     C                   TIME                    $TIMEN
      * Elapsed Time
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
      *
    MU* VAL1($TIMMS) VAL2(7500) COMP(LT)
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
