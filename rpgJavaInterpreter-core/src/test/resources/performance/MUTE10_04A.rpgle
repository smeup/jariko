   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 18/06/19  000908  CM Creato
     V* 19/06/19  V5R1    BMA Check-out 000908 in SMEDEV
     V*=====================================================================
     D* OBIETTIVO
     D*  Programma finalizzato ai test performance sulle stringhe
     V*---------------------------------------------------------------------
      * Considerare i seguenti codici operativi
      *+----------+--+---------!--+
      *!RPGLE     !ST!BUILT-IN !ST!
      *+----------+--+---------+--+
     D $S              S             10  0
     D $C              S             10  0
     D SI              S             10    DIM(17) PERRCD(1) CTDATA             _NOTXT
     D V1              S          30000
      *---------------------------------------------------------------------
     D MSG             S             52
      *---------------------------------------------------------------------
     C                   EXSR      F_CERCA
      *
    MU* Type="NOXMI"
    MU* TIMEOUT(5500)
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test CERCA
      *---------------------------------------------------------------------
     C     F_CERCA       BEGSR
      *
      * carico stringa 30Kb
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
      *---------------------------------------------------------------------
     C                   EVAL      MSG=%SUBST(V1:1:52)
     C     MSG           DSPLY
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
