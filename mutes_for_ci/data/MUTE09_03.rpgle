   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 17/06/19  000908  CM Creato
     V* 19/06/19  V5R1    BMA Check-out 000908 in SMEDEV
     V*=====================================================================
     D* OBIETTIVO
     D*  Programma finalizzato ai test sugli array
     V*---------------------------------------------------------------------
      * Considerare i seguenti codici operativi
      *+----------+--+---------!--+
      *!RPGLE     !ST!BUILT-IN !ST!
      *+-------------+ --------!--+
      *!SORTA     !  !%SUBARR  !  !
      *+----------+--+---------+--+
     D SI              S             10    DIM(17) PERRCD(1) CTDATA             _NOTXT
     D V               S              3  0
     D V1              S             10    DIM(17) ASCEND
     D V2              S             10    DIM(17) DESCEND
     D S1              S             10
     C                   EXSR      F_SORTA
      *
    MU* Type="NOXMI"
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test SORTA
      *---------------------------------------------------------------------
     C     F_SORTA       BEGSR
     C                   EVAL      V1=SI
    MU* VAL1(S1) VAL2('001       ') COMP(EQ)
     C                   EVAL      S1=V1(1)
    MU* VAL1(S1) VAL2('d01       ') COMP(EQ)
     C                   EVAL      S1=V1(2)
    MU* VAL1(S1) VAL2('A01       ') COMP(EQ)
     C                   EVAL      S1=V1(3)
    MU* VAL1(S1) VAL2('c01       ') COMP(EQ)
     C                   EVAL      S1=V1(4)
    MU* VAL1(S1) VAL2('B01       ') COMP(EQ)
     C                   EVAL      S1=V1(5)
    MU* VAL1(S1) VAL2('b01       ') COMP(EQ)
     C                   EVAL      S1=V1(6)
    MU* VAL1(S1) VAL2('C01       ') COMP(EQ)
     C                   EVAL      S1=V1(7)
    MU* VAL1(S1) VAL2('901       ') COMP(EQ)
     C                   EVAL      S1=V1(8)
    MU* VAL1(S1) VAL2('101       ') COMP(EQ)
     C                   EVAL      S1=V1(9)
    MU* VAL1(S1) VAL2('D01       ') COMP(EQ)
     C                   EVAL      S1=V1(10)
    MU* VAL1(S1) VAL2('H01       ') COMP(EQ)
     C                   EVAL      S1=V1(11)
    MU* VAL1(S1) VAL2('E01       ') COMP(EQ)
     C                   EVAL      S1=V1(12)
    MU* VAL1(S1) VAL2('201       ') COMP(EQ)
     C                   EVAL      S1=V1(13)
    MU* VAL1(S1) VAL2('e01       ') COMP(EQ)
     C                   EVAL      S1=V1(14)
    MU* VAL1(S1) VAL2('a01       ') COMP(EQ)
     C                   EVAL      S1=V1(15)
    MU* VAL1(S1) VAL2('          ') COMP(EQ)
     C                   EVAL      S1=V1(16)
    MU* VAL1(S1) VAL2('          ') COMP(EQ)
     C                   EVAL      S1=V1(17)
      *
     C                   SORTA     V1
    MU* VAL1(S1) VAL2('          ') COMP(EQ)
     C                   EVAL      S1=V1(01)
    MU* VAL1(S1) VAL2('          ') COMP(EQ)
     C                   EVAL      S1=V1(02)
    MU* VAL1(S1) VAL2('a01       ') COMP(EQ)
     C                   EVAL      S1=V1(03)
    MU* VAL1(S1) VAL2('b01       ') COMP(EQ)
     C                   EVAL      S1=V1(04)
    MU* VAL1(S1) VAL2('c01       ') COMP(EQ)
     C                   EVAL      S1=V1(05)
    MU* VAL1(S1) VAL2('d01       ') COMP(EQ)
     C                   EVAL      S1=V1(06)
    MU* VAL1(S1) VAL2('e01       ') COMP(EQ)
     C                   EVAL      S1=V1(07)
    MU* VAL1(S1) VAL2('A01       ') COMP(EQ)
     C                   EVAL      S1=V1(08)
    MU* VAL1(S1) VAL2('B01       ') COMP(EQ)
     C                   EVAL      S1=V1(09)
    MU* VAL1(S1) VAL2('C01       ') COMP(EQ)
     C                   EVAL      S1=V1(10)
    MU* VAL1(S1) VAL2('D01       ') COMP(EQ)
     C                   EVAL      S1=V1(11)
    MU* VAL1(S1) VAL2('E01       ') COMP(EQ)
     C                   EVAL      S1=V1(12)
    MU* VAL1(S1) VAL2('H01       ') COMP(EQ)
     C                   EVAL      S1=V1(13)
    MU* VAL1(S1) VAL2('001       ') COMP(EQ)
     C                   EVAL      S1=V1(14)
    MU* VAL1(S1) VAL2('101       ') COMP(EQ)
     C                   EVAL      S1=V1(15)
    MU* VAL1(S1) VAL2('201       ') COMP(EQ)
     C                   EVAL      S1=V1(16)
    MU* VAL1(S1) VAL2('901       ') COMP(EQ)
     C                   EVAL      S1=V1(17)
      *
     C                   EVAL      V1=SI
     C                   SORTA     %SUBARR(V1:1:15)
    MU* VAL1(S1) VAL2('a01       ') COMP(EQ)
     C                   EVAL      S1=V1(01)
    MU* VAL1(S1) VAL2('b01       ') COMP(EQ)
     C                   EVAL      S1=V1(02)
    MU* VAL1(S1) VAL2('c01       ') COMP(EQ)
     C                   EVAL      S1=V1(03)
    MU* VAL1(S1) VAL2('d01       ') COMP(EQ)
     C                   EVAL      S1=V1(04)
    MU* VAL1(S1) VAL2('e01       ') COMP(EQ)
     C                   EVAL      S1=V1(05)
    MU* VAL1(S1) VAL2('A01       ') COMP(EQ)
     C                   EVAL      S1=V1(06)
    MU* VAL1(S1) VAL2('B01       ') COMP(EQ)
     C                   EVAL      S1=V1(07)
    MU* VAL1(S1) VAL2('C01       ') COMP(EQ)
     C                   EVAL      S1=V1(08)
    MU* VAL1(S1) VAL2('D01       ') COMP(EQ)
     C                   EVAL      S1=V1(09)
    MU* VAL1(S1) VAL2('E01       ') COMP(EQ)
     C                   EVAL      S1=V1(10)
    MU* VAL1(S1) VAL2('H01       ') COMP(EQ)
     C                   EVAL      S1=V1(11)
    MU* VAL1(S1) VAL2('001       ') COMP(EQ)
     C                   EVAL      S1=V1(12)
    MU* VAL1(S1) VAL2('101       ') COMP(EQ)
     C                   EVAL      S1=V1(13)
    MU* VAL1(S1) VAL2('201       ') COMP(EQ)
     C                   EVAL      S1=V1(14)
    MU* VAL1(S1) VAL2('901       ') COMP(EQ)
     C                   EVAL      S1=V1(15)
    MU* VAL1(S1) VAL2('          ') COMP(EQ)
     C                   EVAL      S1=V1(16)
    MU* VAL1(S1) VAL2('          ') COMP(EQ)
     C                   EVAL      S1=V1(17)
      * Ricerca
    MU* VAL1(V) VAL2(6) COMP(EQ)
     C                   EVAL      V=%LOOKUP('A01':V1)                          COSTANTE
    MU* VAL1(V) VAL2(6) COMP(EQ)
     C                   EVAL      V=%LOOKUPGT('e01':V1)                        COSTANTE
    MU* VAL1(V) VAL2(6) COMP(EQ)
     C                   EVAL      V=%LOOKUPLT('B01':V1)                        COSTANTE
    MU* VAL1(V) VAL2(5) COMP(EQ)
     C                   EVAL      V=%LOOKUPGE('e01':V1)                        COSTANTE
    MU* VAL1(V) VAL2(7) COMP(EQ)
     C                   EVAL      V=%LOOKUPLE('B01':V1)                        COSTANTE
    MU* VAL1(V) VAL2(0) COMP(EQ)
     C                   EVAL      V=%LOOKUPLE('999':V1)                        COSTANTE
      *
     C                   EVAL      V2=SI
    MU* VAL1(S1) VAL2('001       ') COMP(EQ)
     C                   EVAL      S1=V2(1)
    MU* VAL1(S1) VAL2('d01       ') COMP(EQ)
     C                   EVAL      S1=V2(2)
    MU* VAL1(S1) VAL2('A01       ') COMP(EQ)
     C                   EVAL      S1=V2(3)
    MU* VAL1(S1) VAL2('c01       ') COMP(EQ)
     C                   EVAL      S1=V2(4)
    MU* VAL1(S1) VAL2('B01       ') COMP(EQ)
     C                   EVAL      S1=V2(5)
    MU* VAL1(S1) VAL2('b01       ') COMP(EQ)
     C                   EVAL      S1=V2(6)
    MU* VAL1(S1) VAL2('C01       ') COMP(EQ)
     C                   EVAL      S1=V2(7)
    MU* VAL1(S1) VAL2('901       ') COMP(EQ)
     C                   EVAL      S1=V2(8)
    MU* VAL1(S1) VAL2('101       ') COMP(EQ)
     C                   EVAL      S1=V2(9)
    MU* VAL1(S1) VAL2('D01       ') COMP(EQ)
     C                   EVAL      S1=V2(10)
    MU* VAL1(S1) VAL2('H01       ') COMP(EQ)
     C                   EVAL      S1=V2(11)
    MU* VAL1(S1) VAL2('E01       ') COMP(EQ)
     C                   EVAL      S1=V2(12)
    MU* VAL1(S1) VAL2('201       ') COMP(EQ)
     C                   EVAL      S1=V2(13)
    MU* VAL1(S1) VAL2('e01       ') COMP(EQ)
     C                   EVAL      S1=V2(14)
    MU* VAL1(S1) VAL2('a01       ') COMP(EQ)
     C                   EVAL      S1=V2(15)
    MU* VAL1(S1) VAL2('          ') COMP(EQ)
     C                   EVAL      S1=V2(16)
    MU* VAL1(S1) VAL2('          ') COMP(EQ)
     C                   EVAL      S1=V2(17)
      *
     C                   SORTA     V2
    MU* VAL1(S1) VAL2('901       ') COMP(EQ)
     C                   EVAL      S1=V2(01)
    MU* VAL1(S1) VAL2('201       ') COMP(EQ)
     C                   EVAL      S1=V2(02)
    MU* VAL1(S1) VAL2('101       ') COMP(EQ)
     C                   EVAL      S1=V2(03)
    MU* VAL1(S1) VAL2('001       ') COMP(EQ)
     C                   EVAL      S1=V2(04)
    MU* VAL1(S1) VAL2('H01       ') COMP(EQ)
     C                   EVAL      S1=V2(05)
    MU* VAL1(S1) VAL2('E01       ') COMP(EQ)
     C                   EVAL      S1=V2(06)
    MU* VAL1(S1) VAL2('D01       ') COMP(EQ)
     C                   EVAL      S1=V2(07)
    MU* VAL1(S1) VAL2('C01       ') COMP(EQ)
     C                   EVAL      S1=V2(08)
    MU* VAL1(S1) VAL2('B01       ') COMP(EQ)
     C                   EVAL      S1=V2(09)
    MU* VAL1(S1) VAL2('A01       ') COMP(EQ)
     C                   EVAL      S1=V2(10)
    MU* VAL1(S1) VAL2('e01       ') COMP(EQ)
     C                   EVAL      S1=V2(11)
    MU* VAL1(S1) VAL2('d01       ') COMP(EQ)
     C                   EVAL      S1=V2(12)
    MU* VAL1(S1) VAL2('c01       ') COMP(EQ)
     C                   EVAL      S1=V2(13)
    MU* VAL1(S1) VAL2('b01       ') COMP(EQ)
     C                   EVAL      S1=V2(14)
    MU* VAL1(S1) VAL2('a01       ') COMP(EQ)
     C                   EVAL      S1=V2(15)
    MU* VAL1(S1) VAL2('          ') COMP(EQ)
     C                   EVAL      S1=V2(16)
    MU* VAL1(S1) VAL2('          ') COMP(EQ)
     C                   EVAL      S1=V2(17)
      *
     C                   EVAL      V2=SI
     C                   SORTA     %SUBARR(V2:1:15)
    MU* VAL1(S1) VAL2('901       ') COMP(EQ)
     C                   EVAL      S1=V2(01)
    MU* VAL1(S1) VAL2('201       ') COMP(EQ)
     C                   EVAL      S1=V2(02)
    MU* VAL1(S1) VAL2('101       ') COMP(EQ)
     C                   EVAL      S1=V2(03)
    MU* VAL1(S1) VAL2('001       ') COMP(EQ)
     C                   EVAL      S1=V2(04)
    MU* VAL1(S1) VAL2('H01       ') COMP(EQ)
     C                   EVAL      S1=V2(05)
    MU* VAL1(S1) VAL2('E01       ') COMP(EQ)
     C                   EVAL      S1=V2(06)
    MU* VAL1(S1) VAL2('D01       ') COMP(EQ)
     C                   EVAL      S1=V2(07)
    MU* VAL1(S1) VAL2('C01       ') COMP(EQ)
     C                   EVAL      S1=V2(08)
    MU* VAL1(S1) VAL2('B01       ') COMP(EQ)
     C                   EVAL      S1=V2(09)
    MU* VAL1(S1) VAL2('A01       ') COMP(EQ)
     C                   EVAL      S1=V2(10)
    MU* VAL1(S1) VAL2('e01       ') COMP(EQ)
     C                   EVAL      S1=V2(11)
    MU* VAL1(S1) VAL2('d01       ') COMP(EQ)
     C                   EVAL      S1=V2(12)
    MU* VAL1(S1) VAL2('c01       ') COMP(EQ)
     C                   EVAL      S1=V2(13)
    MU* VAL1(S1) VAL2('b01       ') COMP(EQ)
     C                   EVAL      S1=V2(14)
    MU* VAL1(S1) VAL2('a01       ') COMP(EQ)
     C                   EVAL      S1=V2(15)
    MU* VAL1(S1) VAL2('          ') COMP(EQ)
     C                   EVAL      S1=V2(16)
    MU* VAL1(S1) VAL2('          ') COMP(EQ)
     C                   EVAL      S1=V2(17)
      * Ricerca
    MU* VAL1(V) VAL2(10) COMP(EQ)
     C                   EVAL      V=%LOOKUP('A01':V2)                          COSTANTE
    MU* VAL1(V) VAL2(10) COMP(EQ)
     C                   EVAL      V=%LOOKUPGT('e01':V2)                        COSTANTE
    MU* VAL1(V) VAL2(10) COMP(EQ)
     C                   EVAL      V=%LOOKUPLT('B01':V2)                        COSTANTE
    MU* VAL1(V) VAL2(11) COMP(EQ)
     C                   EVAL      V=%LOOKUPGE('e01':V2)                        COSTANTE
    MU* VAL1(V) VAL2(09) COMP(EQ)
     C                   EVAL      V=%LOOKUPLE('B01':V2)                        COSTANTE
    MU* VAL1(V) VAL2(0) COMP(EQ)
     C                   EVAL      V=%LOOKUPLE('999':V2)                        COSTANTE
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


