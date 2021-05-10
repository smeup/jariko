      *---------------------------------------------------------------
      * Program used to measure time performance of an RPG procedure
      * call iteration.
      *---------------------------------------------------------------
     DRPG_SUM          PR
     DpN1                             9  0
     DpN2                             9  0
      *---------------------------------------------------------------
     D N1              S              9  0
     D N2              S              9  0
     D TOT             S              9  0
     D LOOP            S             11  0
     D CYCLED          S             11  0
      *---------------------------------------------------------------
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
      *---------------------------------------------------------------
     D MSG             S             52
     D CYC             S             50
     D ELA             S             50
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * Start time
     C                   EVAL      N1=1
     C                   EVAL      N2=0
     C                   EVAL      LOOP=100000
      *
     C                   TIME                    $TIMST
      *
     C     1             DO        LOOP          CYCLED
     C                   CALLP     RPG_SUM(N1:N2)
     C                   EVAL      TOT=N2
     C                   EVAL      N1=N2
     C                   ENDDO
      * End time
     C                   TIME                    $TIMEN
      * Elapsed time
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
      * Display message
     C                   EVAL      CYC='Cycled=' + %CHAR(CYCLED)
     C                   EVAL      ELA='Elapsed=' +
     C                               %TRIM(%EDITC($TIMMS:'Q')) + 'ms '
     C                   EVAL      MSG='RPG_SUM : ' +
     C                                  %TRIM(CYC) + ' ' +
     C                                  %TRIM(ELA) + ' ' +
     C                                  ' Tot=' + %CHAR(TOT)
     C     MSG           DSPLY
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
     PRPG_SUM          B
     DRPG_SUM          PI
     DpN1                             9  0
     DpN2                             9  0
     C                   EVAL      pN2=pN1+1
     PRPG_SUM          E
      *---------------------------------------------------------------