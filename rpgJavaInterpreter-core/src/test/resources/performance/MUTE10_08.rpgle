     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 16/12/19  001378  BMA   Creato
     V*=====================================================================
     D* OBIETTIVO
     D*  Programma finalizzato ai test performance su Statement vari
     V*---------------------------------------------------------------------
      * Considerare i seguenti codici operativi
      *+----------+--+---------!--+
      *!RPGLE     !ST!BUILT-IN !ST!
      *+-------------+ --------!--+
      *+----------+--+---------+--+
     D $CICL           S             10I 0
     D $N1             S              5I 0
     D $V              S              5I 0
     D V1              S          30000
     D S1              S            100    INZ('TEST performance')
     D V2              S          30000    Varying
     D TXT             S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
     D RES             S            100    DIM(10)
     D TMP             S            100
     D ST1             S            100
     D ST2             S            100
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10I 0
     D$MSG             S             52
      *
      * Main
     C                   EXSR      EXECUTE
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test su statement diversi
      *---------------------------------------------------------------------
     C     EXECUTE       BEGSR
      *
      * Entry parameters for loop
     C     *ENTRY        PLIST
     C                   PARM                    $CICL
      * Start time
     C                   TIME                    $TIMST
     C                   CLEAR                   AAA001            1
      * Loop
     C                   DO        $CICL
      *
     C                   IF        AAA001='A'
     C                   EVAL      AAA001='B'
     C                   ELSE
     C                   EVAL      AAA001='A'
     C                   ENDIF
     C                   SELECT
     C                   WHEN      AAA001='A'
     C                   EVAL      RES(1)=TXT(1)
     C                   EVAL      ST1=RES(1)
     C                   MOVEL     ST1           ST2
     C                   OTHER
     C                   EVAL      RES(2)=TXT(1)
     C                   EVAL      ST1=RES(2)
     C                   MOVE      ST1           ST2
     C                   ENDSL
     C                   IF        AAA001='A'
     C                   CLEAR                   RES
     C                   EVAL      RES(5)=%SUBST(TXT(1):1:5)
     C                   ELSE
     C                   EVAL      $V=%LOOKUP('TEST':TXT:1)
     C                   ENDIF
     C     AAA001        IFNE      'B'
     C                   EVAL      $V=%LOOKUP('TEST':TXT:1)
     C                   ELSE
     C                   CLEAR                   RES
     C                   EVAL      RES(5)=%SUBST(TXT(1):1:5)
     C                   ENDIF
      *
     C                   EXSR      SR_02
      *
      * End primary loop
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
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD*    Subroutine 02
      *--------------------------------------------------------------*
     C     SR_02         BEGSR
      *
     C                   EVAL      V1=%TRIMR(S1)
     C                   EVAL      V2=%TRIM(S1)
     C                   EVAL      TMP=TXT(1)
     C                   EVAL      $N1=%SCAN('e':TMP)
      *
     C                   ENDSR
** TXT
Time spent




TEST
