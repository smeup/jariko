     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 29/03/21  002768  BERNI Creato
     V* 29/03/21  V5R1    BMA   Check-out 002768 in SMEDEV
     V*=====================================================================
     D XTIMST          S             30    INZ
     D XTIMEN          S             30    INZ
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10I 0
     D $TIMSS          S             10I 0
     D $TIMMN          S             10I 0
     D $TIMHR          S             10I 0
     D $TIMDY          S             10I 0
     D $TIMMT          S             10I 0
     D $TIMYS          S             10I 0
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      *
      * Calculate the number of Milliseconds between two time
     C                   EVAL      XTIMST='2021-03-29-16.43.15.725000'
     C                   EVAL      $TIMST=%TIMESTAMP(XTIMST)
     C                   EVAL      XTIMEN='2021-03-29-16.44.01.538000'
     C                   EVAL      $TIMEN=%TIMESTAMP(XTIMEN)
      * Elapsed time
    MU* VAL1($TIMMS) VAL2('45813000  ') COMP(EQ)
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
      *
      * Calculate the number of Seconds between two time
      * Elapsed time
    MU* VAL1($TIMSS) VAL2('45        ') COMP(EQ)
     C     $TIMEN        SUBDUR    $TIMST        $TIMSS:*S
      *
      * Calculate the number of Minutes between two time
     C                   EVAL      XTIMST='2021-03-29-12.43.15.725000'
     C                   EVAL      $TIMST=%TIMESTAMP(XTIMST)
     C                   EVAL      XTIMEN='2021-03-29-16.44.01.538000'
     C                   EVAL      $TIMEN=%TIMESTAMP(XTIMEN)
      * Elapsed time
    MU* VAL1($TIMMN) VAL2('240       ') COMP(EQ)
     C     $TIMEN        SUBDUR    $TIMST        $TIMMN:*MN
      *
      * Calculate the number of Hours between two time
      * Elapsed time
    MU* VAL1($TIMHR) VAL2('4         ') COMP(EQ)
     C     $TIMEN        SUBDUR    $TIMST        $TIMHR:*H
      *
      * Calculate the number of Days between two time
     C                   EVAL      XTIMST='2019-03-29-12.43.15.725000'
     C                   EVAL      $TIMST=%TIMESTAMP(XTIMST)
     C                   EVAL      XTIMEN='2021-03-29-16.44.01.538000'
     C                   EVAL      $TIMEN=%TIMESTAMP(XTIMEN)
      * Elapsed time
    MU* VAL1($TIMDY) VAL2('731       ') COMP(EQ)
     C     $TIMEN        SUBDUR    $TIMST        $TIMDY:*D
      *
      * Calculate the number of Months between two time
      * Elapsed time
    MU* VAL1($TIMMT) VAL2('24        ') COMP(EQ)
     C     $TIMEN        SUBDUR    $TIMST        $TIMMT:*M
      *
      * Calculate the number of Years between two time
      * Elapsed time
    MU* VAL1($TIMYS) VAL2('2         ') COMP(EQ)
     C     $TIMEN        SUBDUR    $TIMST        $TIMYS:*Y
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
