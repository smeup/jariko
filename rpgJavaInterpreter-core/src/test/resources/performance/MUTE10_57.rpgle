     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 06/05/20  001859 LANMAM Creato
     V*=====================================================================
     D TXT             S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
     D$MSG             S             52
     D $X              S             10I 0
     D $VAR01          S             10I 0
     D $VAR02          S             10I 0
     D $VAR03          S             10I 0
     D $VAR04          S             10I 0
     D $VAR05          S             10I 0
     D $VAR06          S             10I 0
     D $VAR07          S             10I 0
     D $VAR08          S             10I 0
     D $VAR09          S             10I 0
     D $VAR10          S             10I 0
     D $VAR11          S             10I 0
     D $VAR12          S             10I 0
     D $VAR13          S             10I 0
     D $VAR14          S             10I 0
     D $VAR15          S             10I 0
     D $VAR16          S             10I 0
     D $VAR17          S             10I 0
     D $VAR18          S             10I 0
     D $VAR19          S             10I 0
     D $VAR20          S             10I 0
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * Start time
     C                   TIME                    $TIMST
     C                   DOU       $X=10000001
     C                   EVAL      $X=$X+1
     C                   EVAL      $VAR01=$X
     C                   EVAL      $VAR02=$X
     C                   EVAL      $VAR03=$X
     C                   EVAL      $VAR04=$X
     C                   EVAL      $VAR05=$X
     C                   EVAL      $VAR06=$X
     C                   EVAL      $VAR07=$X
     C                   EVAL      $VAR08=$X
     C                   EVAL      $VAR09=$X
     C                   EVAL      $VAR10=$X
     C                   EVAL      $VAR11=$X
     C                   EVAL      $VAR12=$X
     C                   EVAL      $VAR13=$X
     C                   EVAL      $VAR14=$X
     C                   EVAL      $VAR15=$X
     C                   EVAL      $VAR16=$X
     C                   EVAL      $VAR17=$X
     C                   EVAL      $VAR18=$X
     C                   EVAL      $VAR19=$X
     C                   EVAL      $VAR20=$X
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
    MU* TIMEOUT(260)
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
** TXT
Time spent
