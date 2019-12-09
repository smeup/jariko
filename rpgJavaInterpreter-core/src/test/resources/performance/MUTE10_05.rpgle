     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 05/12/19  001345  BERNI Creato
     V* 09/12/19  001345  BMA   Alcune modifiche
     V* 09/12/19  V5R1    BMA   Check-out 001345 in SMEUP_TST
     V*=====================================================================
     D* OBIETTIVO
     D*  Programma finalizzato ai test performance su Statement vari
     V*---------------------------------------------------------------------
      * Considerare i seguenti codici operativi
      *+----------+--+---------!--+
      *!RPGLE     !ST!BUILT-IN !ST!
      *+-------------+ --------!--+
      *+----------+--+---------+--+
     D $S              S             10  0
     D $C              S             10  0
     D $V              S             10  0
     D $N1             S             21  6
     D $N2             S             21  6
     D $N3             S             21  6
     D $CICL           S              7  0
     D V1              S          30000
     D S1              C                   'TEST performance'                   COSTANTE
     D V2              S          30000    Varying
     D TXT             S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
     D RES             S            100    DIM(10)
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
     D$MSG             S             52
      *
      * Main
     C                   EXSR      EXECUTE
      *
    MU* Type="NOXMI"
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test su statement diversi
      *---------------------------------------------------------------------
     C     EXECUTE       BEGSR
      *
      * In input ricevo il numero di volte che voglio ciclare
     C     *ENTRY        PLIST
     C                   PARM                    $CICL
      * rilevo TIME iniziale
     C                   TIME                    $TIMST
      * Ciclo
     C                   DO        $CICL
      *
     C                   Z-SUB     0             $S
     C                   EVAL      $C=1
     C                   ADD       1             $S
      *
     C                   SELECT
     C                   WHEN      $S > 20
     C                   MOVEL     TXT(1)        RES(1)
     C     $S            MULT      $C            $V
     C                   OTHER
     C                   MOVE      TXT(1)        RES(2)
     C                   Z-ADD     1             $V
     C                   ENDSL
      *
     C                   IF        $S=$C
     C                   CLEAR                   RES
     C                   EVAL      RES(5)=%SUBST(TXT(1):1:5)
     C                   ELSE
     C                   EVAL      $V=%LOOKUP('PROVA':TXT:1)
     C                   ENDIF
      *
     C     $S            IFNE      $C
     C                   EVAL      $V=%LOOKUP('PROVA':TXT:1)
     C                   ELSE
     C                   CLEAR                   RES
     C                   EVAL      RES(5)=%SUBST(TXT(1):1:5)
     C                   ENDIF
      *
     C                   EXSR      SR_01
     C                   EXSR      SR_02
      *
      * fine ciclo principale
     C                   ENDDO
      * Calcolo istante di fine
     C                   TIME                    $TIMEN
      * calcolo tempo intercorso
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
    MU* VAL1($TIMMS) VAL2(1000) COMP(LT)
     C                   EVAL      $TIMMS=$TIMMS/1000
      * Espongo il messaggio
     C                   EVAL      $MSG=%trim(TXT(1))+' '+
     C                             %TRIM(%EDITC($TIMMS:'Q'))+'ms'
     C     $MSG          DSPLY     £PDSSU
     C                   ENDSR
      *---------------------------------------------------------------------
    RD*    Subroutine 01
      *--------------------------------------------------------------*
     C     SR_01         BEGSR
      *
     C                   EVAL      $N1=123456,85
     C                   EVAL      $N2=34,678
     C     $N2           DIV(H)    $N3           $N1
     C                   EVAL(H)   $N1=$N2/$N3
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
     C                   EVAL      $N1=%SCAN('a':TXT(1))
      *
     C                   ENDSR
** TXT
Tempo impiegato




PROVA
