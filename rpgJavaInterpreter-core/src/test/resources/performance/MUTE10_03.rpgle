   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 18/06/19  000908  CM Creato
     V* 19/06/19  V5R1    BMA Check-out 000908 in SMEDEV
     V* 28/10/19  001213 BMA Aggiunta annotation TIMEOUT
     V* 29/10/19  V5R1    BERNI Check-out 001213 in SMEDEV
     V* 04/05/20  001855 BMA Tolte annotation di valutazione (non sensate in pgm performance)
     V*=====================================================================
     D* OBIETTIVO
     D*  Programma finalizzato ai test performance sugli array
     V*---------------------------------------------------------------------
      * Considerare i seguenti codici operativi
      *+----------+--+---------!--+
      *!RPGLE     !ST!BUILT-IN !ST!
      *+-------------+ --------!--+
      *!LOOKUP    !  !%LOOKUP  !  !
      *+----------+--+---------+--+
     D $I              S             10  0
     D $S              S             10  0
     D $C              S              6  0
     D SI              S             10    DIM(17) PERRCD(1) CTDATA             _NOTXT
     D V1              S          10000    DIM(1500)
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
     C                   EXSR      F_CERCA
      *
      * TIMEOUT(500)
    MU* TIMEOUT(700)
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test CERCA
      *---------------------------------------------------------------------
     C     F_CERCA       BEGSR
      *
      * carico
     C                   TIME                    $TIMST
     C                   EVAL      $I=1
     C                   EVAL      $C=1
     C                   EVAL      $S=0
     C                   DO        *HIVAL
     C                   IF        $I=1500
     C                   LEAVE
     C                   ENDIF
     C                   EVAL      $S=$S+1
     C                   IF        $S > 17
     C                   EVAL      $S=1
     C                   EVAL      $C=$C+1
     C                   ENDIF
     C                   EVAL      V1($I)=%TRIM(SI($S))+'.'+%EDITC($C:'X')
     C                   EVAL      $I=$I+1
     C                   ENDDO
     C                   TIME                    $TIMEN
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
      *
      * Ricerca
     C                   TIME                    $TIMST
     C                   EVAL      $S=%LOOKUP('A01.029412':V1)
     C                   EVAL      $S=%LOOKUP('A01.027412':V1)
     C                   EVAL      $S=%LOOKUP('A01.000001':V1)
     C                   EVAL      $S=%LOOKUP('A01.020001':V1)
     C                   EVAL      $S=%LOOKUP('A01.780001':V1)
     C                   TIME                    $TIMEN
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
      *
      * Ordino
     C                   TIME                    $TIMST
     C                   SORTA     V1
     C                   TIME                    $TIMEN
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
      *
      * Pulisco
     C                   TIME                    $TIMST
     C                   EVAL      V1=''
     C                   TIME                    $TIMEN
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
     C                   ENDSR
      *---------------------------------------------------------------------
** SI
001
d01
A01
c01
B01
b01
C01
901
101
D01
H01
E01
201
e01
a01
x01
X01
