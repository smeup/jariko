     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 13/03/20  001693 BMA Creato
     V*=====================================================================
     D  ARRAY          S              1    DIM(10) ASCEND
     D TXT             S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
     D$MSG             S             52
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
     C                   EVAL      ARRAY(1)='B'
     C                   EVAL      ARRAY(2)='C'
     C                   EVAL      ARRAY(3)='D'
     C                   EVAL      ARRAY(4)='G'
     C                   EVAL      ARRAY(5)='H'
     C                   EVAL      ARRAY(6)='I'
     C                   EVAL      ARRAY(7)='A'
     C                   EVAL      ARRAY(8)='B'
     C                   EVAL      ARRAY(9)='D'
     C                   SETOFF                                           22
     C
      * Start time
     C                   TIME                    $TIMST
     C                   DO        1000000
     C     'A'           LOOKUP    ARRAY                                  22
     C     'Z'           LOOKUP    ARRAY                                  22
     C     'A'           LOOKUP    ARRAY(8)                               22
     C     'C'           LOOKUP    ARRAY(4)                               22
     C     'J'           LOOKUP    ARRAY                                2122
     C     'J'           LOOKUP    ARRAY                                2122
     C     'J'           LOOKUP    ARRAY(8)                             2122
     C     'J'           LOOKUP    ARRAY(8)                             2122
     C     'I'           LOOKUP    ARRAY(8)                             2122
     C     'I'           LOOKUP    ARRAY(8)                             2122
     C     'A'           LOOKUP    ARRAY(6)                             2122
     C     'A'           LOOKUP    ARRAY(6)                             2122
     C     'E'           LOOKUP    ARRAY                              20  22
     C     'E'           LOOKUP    ARRAY                              20  22
     C     'J'           LOOKUP    ARRAY(8)                           20  22
     C     'J'           LOOKUP    ARRAY(8)                           20  22
     C     'E'           LOOKUP    ARRAY(3)                           20  22
     C     'E'           LOOKUP    ARRAY(3)                           20  22
     C     'I'           LOOKUP    ARRAY(9)                           20  22
     C     'I'           LOOKUP    ARRAY(9)                           20  22
     C     'A'           LOOKUP    ARRAY(7)                           20  22
     C     'A'           LOOKUP    ARRAY(7)                           20  22
     C                   ENDDO
      * End time
     C                   TIME                    $TIMEN
      * Elapsed time
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
      * Display message
     C                   EVAL      $MSG=%trim(TXT(1))+' '+
     C                             %TRIM(%EDITC($TIMMS:'Q'))+'ms'
     C     $MSG          DSPLY     £PDSSU
    MU* TIMEOUT(1300)
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
** TXT
Time spent
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
