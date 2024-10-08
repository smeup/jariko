      *====================================================================
      * smeup V6R1.025DV
      * Nome sorgente       : MUTE10_05A
      * Sorgente di origine : QTEMP/MUSRC(MUTE10_05A)
      * Esportato il        : 20241004 155649
      *====================================================================
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 05/12/19  001345  BERNI Creato
     V* 09/12/19  001345  BMA   Alcune modifiche
     V* 09/12/19  V5R1    BMA   Check-out 001345 in SMEUP_TST
     V* 11/12/19  001362  BERNI Inseriti commenti
     V* 11/12/19  V5R1    BMA   Check-out 001362 in SMEUP_TST
     V* 13/12/19  001378  BMA   Corretta annotation
     V* 17/12/19  V5R1    PEDSTE Check-out 001378 in SMEUP_TST
     V* 23/12/19  V5R1    PEDSTE Check-out 001345 001362 001378 in SMEDEV
     V* 04/03/21  002673 BMA Tolte annotation NOXMI e variati timeout
     V*  5/03/21  V5R1    BERNIC Check-out 002673 in SMEDEV
     V* 07/09/23  005098  BERNI Rinominato pertendo dal MUTE10_05 per correggere la nomenclatura
     V* 07/09/23  V6R1    BMA Check-out 005098 in SMEDEV
     V*=====================================================================
     D* OBIETTIVO
     D*  Programma finalizzato ai test performance su Statement vari
     D*---------------------------------------------------------------------
      * Considerare i seguenti codici operativi
      *+----------+--+---------!--+
      *!RPGLE     !ST!BUILT-IN !ST!
      *+-------------+ --------!--+
      *+----------+--+---------+--+
     D $S              S             10  0
     D $C              S             10  0
     D $V              S             10  0
     D $N1             S             19  6
     D $N2             S             19  6
     D $N3             S             19  6
     D $CICL           S              7  0
     D V1              S          30000
     D S1              S            100    INZ('TEST performance')              COSTANTE
     D V2              S          30000    Varying
     D TXT             S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
     D RES             S            100    DIM(10)
     D ST1             S            100
     D ST2             S            100
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
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
      * Loop
     C                   DO        $CICL
      * Miscellaneus operations
     C                   Z-SUB     0             $S
     C                   EVAL      $C=1
     C                   ADD       1             $S
      *
     C                   SELECT
     C                   WHEN      $S > 20
     C                   EVAL      RES(1)=TXT(1)
     C                   EVAL      ST1=RES(1)
     C                   MOVEL     ST1           ST2
     C     $S            MULT      $C            $V
     C                   OTHER
     C                   EVAL      RES(2)=TXT(1)
     C                   EVAL      ST1=RES(2)
     C                   MOVE      ST1           ST2
     C                   Z-ADD     1             $V
     C                   ENDSL
      *
     C                   IF        $S=$C
     C                   CLEAR                   RES
     C                   EVAL      RES(5)=%SUBST(TXT(1):1:5)
     C                   ELSE
     C                   EVAL      $V=%LOOKUP('TEST':TXT:1)
     C                   ENDIF
      *
     C     $S            IFNE      $C
     C                   EVAL      $V=%LOOKUP('TEST':TXT:1)
     C                   ELSE
     C                   CLEAR                   RES
     C                   EVAL      RES(5)=%SUBST(TXT(1):1:5)
     C                   ENDIF
      *
     C                   EXSR      SR_01
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
    RD*    Subroutine 01
      *--------------------------------------------------------------*
     C     SR_01         BEGSR
      *
     C                   EVAL      $N1=123456,85
     C                   EVAL      $N2=34,678
     C     $N1           DIV(H)    $N2           $N3
     C                   EVAL(H)   $N3=$N1/$N2
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD*    Subroutine 02
      *--------------------------------------------------------------*
     C     SR_02         BEGSR
      *
     C                   EVAL      V1=%TRIMR(S1)
     C                   EVAL      V2=%TRIM(S1)
     C                   EVAL      V1=%EDITC($N1:'P')
     C                   EVAL      V1=%CHAR($N1)
     C                   EVAL      ST1=TXT(1)
     C                   EVAL      $N1=%SCAN('a':ST1)
      *
     C                   ENDSR
** TXT
Time spent




TEST
