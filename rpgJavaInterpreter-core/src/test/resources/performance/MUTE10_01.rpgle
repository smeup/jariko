     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
     C                   EXSR      F_CALL
      *
    MU* Type="NOXMI"
    MU* TIMEOUT(7500)
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test
      *---------------------------------------------------------------------
     C     F_CALL        BEGSR
     C                   TIME                    $TIMST
     C                   DO        1000
     C                   CALL      'MUTE10_01A'
     C                   ENDDO
     C                   TIME                    $TIMEN
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
    MU* VAL1($TIMMS) VAL2(7500) COMP(LT)
     C                   EVAL      $TIMMS=$TIMMS/1000
     C                   ENDSR
