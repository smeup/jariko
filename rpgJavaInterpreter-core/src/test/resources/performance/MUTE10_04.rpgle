     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 18/06/19  000908  CM Creato
     V* 19/06/19  V5R1    BMA Check-out 000908 in SMEDEV
     V* 19/07/19  001014 BMA Aggiunti dsply e trace esecuzione
     V* 19/07/19  V5R1    DOSSTE Check-out 001014 in SMEDEV
     V* 28/10/19  001213 BMA Aggiunta annotation TIMEOUT
     V* 29/10/19  V5R1    BERNI Check-out 001213 in SMEDEV
     V* 04/05/20  001855 BMA Tolte annotation di valutazione (non sensate in pgm performance)
     V*=====================================================================
     D* OBIETTIVO
     D*  Programma finalizzato ai test performance sulle stringhe
     V*---------------------------------------------------------------------
      * Considerare i seguenti codici operativi
      *+----------+--+---------!--+
      *!RPGLE     !ST!BUILT-IN !ST!
      *+-------------+ --------!--+
      *!LOOKUP    !  !%LOOKUP  !  !
      *+----------+--+---------+--+
     D $S              S             10  0
     D $C              S             10  0
     D $V              S             10  0
     D SI              S             10    DIM(17) PERRCD(1) CTDATA             _NOTXT
     D TXT             S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
     D V1              S          30000
     D S1              S            100
     D V2              S          30000    Varying
     D I1              S              5  0
     D $R              S             10  0
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
     D$MSG             S             52
     C                   EXSR      F_CERCA
      *
    MU* TIMEOUT(9000)
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test CERCA
      *---------------------------------------------------------------------
     C     F_CERCA       BEGSR
      *
      * carico stringa 30Kb
     C                   TIME                    $TIMST
     C                   EVAL      $S=0
     C                   EVAL      $C=1
     C                   DOW       %LEN(%TRIM(V1)) < 30000
     C                   EVAL      $S=$S+1
     C                   IF        $S > 17
     C                   EVAL      $S=1
     C                   EVAL      $C=$C+1
     C                   ENDIF
     C                   EVAL      V1=%TRIM(V1)+%TRIM(SI($S))
     C                               +'.'+%TRIM(%EDITC($C:'Z'))+'_'
     C                   ENDDO
     C                   TIME                    $TIMEN
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
     C                   EVAL      $MSG=%trim(TXT(1))+' '+
     C                             %TRIM(%EDITC($TIMMS:'Q'))
     C     $MSG          DSPLY     £PDSSU
      *
      * carico stringa 30Kb varying
     C                   TIME                    $TIMST
     C                   EVAL      $S=0
     C                   EVAL      $C=1
     C                   DOW       %LEN(V2) < 30000
     C                   EVAL      $S=$S+1
     C                   IF        $S > 17
     C                   EVAL      $S=1
     C                   EVAL      $C=$C+1
     C                   ENDIF
     C                   EVAL      V2=V2+%TRIM(SI($S))
     C                               +'.'+%TRIM(%EDITC($C:'Z'))+'_'
     C                   ENDDO
     C                   TIME                    $TIMEN
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
     C                   EVAL      $MSG=%trim(TXT(2))+' '+
     C                             %TRIM(%EDITC($TIMMS:'Q'))
     C     $MSG          DSPLY     £PDSSU
      *
      * Ricerco su V1
     C                   TIME                    $TIMST
     C                   EVAL      $R=0
     C                   EVAL      $S=0
     C                   EVAL      $C=1
     C                   DO        4000
     C                   EVAL      $S=$S+1
     C                   IF        $S > 17
     C                   EVAL      $S=1
     C                   EVAL      $C=$C+1
     C                   ENDIF
     C                   EVAL      S1=%TRIM(SI($S))+'.'+%TRIM(%EDITC($C:'Z'))
     C                   EVAL      $V=%SCAN(%TRIM(S1):V1)
     C                   IF        $V>0
     C                   EVAL      $R=$R+$V
     C                   ENDIF
     C                   ENDDO
     C                   TIME                    $TIMEN
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
     C                   EVAL      $MSG=%trim(TXT(3))+' '+
     C                             %TRIM(%EDITC($TIMMS:'Q'))
     C     $MSG          DSPLY     £PDSSU
     C                   EVAL      $MSG=%trim(TXT(7))+' '+%EDITC($R:'Q')
     C     $MSG          DSPLY     £PDSSU
      *
      * Ricerco su V2
     C                   TIME                    $TIMST
     C                   EVAL      $R=0
     C                   EVAL      $S=0
     C                   EVAL      $C=1
     C                   DO        4000
     C                   EVAL      $S=$S+1
     C                   IF        $S > 17
     C                   EVAL      $S=1
     C                   EVAL      $C=$C+1
     C                   ENDIF
     C                   EVAL      S1=%TRIM(SI($S))+'.'+%TRIM(%EDITC($C:'Z'))
     C                   EVAL      $V=%SCAN(%TRIM(S1):V2)
     C                   IF        $V>0
     C                   EVAL      $R=$R+$V
     C                   ENDIF
     C                   ENDDO
     C                   TIME                    $TIMEN
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
     C                   EVAL      $MSG=%trim(TXT(4))+' '+
     C                             %TRIM(%EDITC($TIMMS:'Q'))
     C     $MSG          DSPLY     £PDSSU
     C                   EVAL      $MSG=%trim(TXT(7))+' '+%EDITC($R:'Q')
     C     $MSG          DSPLY     £PDSSU
      *
      * Ricerco su V1 con SUBSTR
     C                   TIME                    $TIMST
     C                   EVAL      $R=0
     C                   EVAL      $S=0
     C                   EVAL      $C=1
     C                   DO        4000
     C                   EVAL      $S=$S+1
     C                   IF        $S > 17
     C                   EVAL      $S=1
     C                   EVAL      $C=$C+1
     C                   ENDIF
     C                   EVAL      S1=%TRIM(SI($S))+'.'+%TRIM(%EDITC($C:'Z'))
     C                   EVAL      I1=$C*10
     C                   IF        I1 > 29900
     C                   EVAL      I1=29900
     C                   ENDIF
     C                   EVAL      $V=%SCAN(%TRIM(S1):V1:I1)
     C                   IF        $V>0
     C                   EVAL      $R=$R+$V
     C                   ENDIF
     C                   ENDDO
     C                   TIME                    $TIMEN
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
     C                   EVAL      $MSG=%trim(TXT(5))+' '+
     C                             %TRIM(%EDITC($TIMMS:'Q'))
     C     $MSG          DSPLY     £PDSSU
     C                   EVAL      $MSG=%trim(TXT(7))+' '+%EDITC($R:'Q')
     C     $MSG          DSPLY     £PDSSU
      *
      * Ricerco su V2 con SUBSTR
     C                   TIME                    $TIMST
     C                   EVAL      $R=0
     C                   EVAL      $S=0
     C                   EVAL      $C=1
     C                   DO        4000
     C                   EVAL      $S=$S+1
     C                   IF        $S > 17
     C                   EVAL      $S=1
     C                   EVAL      $C=$C+1
     C                   ENDIF
     C                   EVAL      S1=%TRIM(SI($S))+'.'+%TRIM(%EDITC($C:'Z'))
     C                   EVAL      I1=$C*10
     C                   IF        I1 > 29900
     C                   EVAL      I1=29900
     C                   ENDIF
     C                   EVAL      $V=%SCAN(%TRIM(S1):V2:I1)
     C                   IF        $V>0
     C                   EVAL      $R=$R+$V
     C                   ENDIF
     C                   ENDDO
     C                   TIME                    $TIMEN
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
     C                   EVAL      $MSG=%trim(TXT(6))+' '+
     C                             %TRIM(%EDITC($TIMMS:'Q'))
     C     $MSG          DSPLY     £PDSSU
     C                   EVAL      $MSG=%trim(TXT(7))+' '+%EDITC($R:'Q')
     C     $MSG          DSPLY     £PDSSU
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
** TXT
Ms Loop stringa 30K
Ms Loop stringa 30K varying
Ms Scan stringa 30K
Ms Scan stringa 30K varying
Ms Scan con len stringa 30K
Ms Scan con len stringa 30K varying
Risultato scan:
