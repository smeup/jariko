   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 17/06/19  000908  CM Creato
     V* 19/06/19  V5R1    BMA Check-out 000908 in SMEDEV
     V*=====================================================================
     D* OBIETTIVO
     D*  Programma finalizzato ai test sugli arrotondamenti eseguiti tramite EVAL(x)
     V*---------------------------------------------------------------------
      * Considerare i seguenti codici operativi
      *+----------+--+---------!--+
      *!RPGLE     !ST!BUILT-IN !ST!
      *+-------------+ --------!--+
      *!EVAL      !  !         !  !
      *+----------+--+---------+--+
     D V1              S             26  6
     D V2              S             26  6
     D V2A             S             26  6
     D V3              S             26  2
     D V4              S             26  6
     C                   EXSR      F_EVAL
      *
    MU* Type="NOXMI"
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test EVAL
      *---------------------------------------------------------------------
     C     F_EVAL        BEGSR
    MU* VAL1(V1) VAL2(10,54897) COMP(EQ)
     C                   EVAL      V1=10,54897
    MU* VAL1(V2) VAL2(7,71897) COMP(EQ)
     C                   EVAL      V2=7,71897
    MU* VAL1(V2A) VAL2(3,71897) COMP(EQ)
     C                   EVAL      V2A=3,71897
    MU* VAL1(V3) VAL2(302,82) COMP(EQ)
     C                   EVAL      V3=V1*V2*V2A
    MU* VAL1(V4) VAL2(302,825) COMP(EQ)
     C                   EVAL      V4=V1*V2*V2A
    MU* VAL1(V3) VAL2(302,82) COMP(EQ)
     C                   EVAL(R)   V3=V1*V2*V2A
    MU* VAL1(V4) VAL2(302,82525) COMP(EQ)
     C                   EVAL(R)   V4=V1*V2*V2A
    MU* VAL1(V3) VAL2(302,83) COMP(EQ)
     C                   EVAL(H)   V3=V1*V2*V2A
    MU* VAL1(V4) VAL2(302,825) COMP(EQ)
     C                   EVAL(H)   V4=V1*V2*V2A
    MU* VAL1(V3) VAL2(302,83) COMP(EQ)
     C                   EVAL(RH)  V3=V1*V2*V2A
    MU* VAL1(V4) VAL2(302,825251) COMP(EQ)
     C                   EVAL(RH)  V4=V1*V2*V2A
    MU* VAL1(V3) VAL2(,36) COMP(EQ)
     C                   EVAL      V3=V1/V2/V2A
    MU* VAL1(V4) VAL2(,367475) COMP(EQ)
     C                   EVAL      V4=V1/V2/V2A
    MU* VAL1(V3) VAL2(,36) COMP(EQ)
     C                   EVAL(R)   V3=V1/V2/V2A
    MU* VAL1(V4) VAL2(,367475) COMP(EQ)
     C                   EVAL(R)   V4=V1/V2/V2A
    MU* VAL1(V3) VAL2(,37) COMP(EQ)
     C                   EVAL(H)   V3=V1/V2/V2A
    MU* VAL1(V4) VAL2(,367475) COMP(EQ)
     C                   EVAL(H)   V4=V1/V2/V2A
    MU* VAL1(V3) VAL2(,37) COMP(EQ)
     C                   EVAL(RH)  V3=V1/V2/V2A
    MU* VAL1(V4) VAL2(,367475) COMP(EQ)
     C                   EVAL(RH)  V4=V1/V2/V2A
      *
    MU* VAL1(V1) VAL2(10,54897) COMP(EQ)
     C                   EVAL      V1=8910,54897
    MU* VAL1(V2) VAL2(7,71897) COMP(EQ)
     C                   EVAL      V2=5897,71897
    MU* VAL1(V2A) VAL2(3,71897) COMP(EQ)
     C                   EVAL      V2A=3,71897
    MU* VAL1(V3) VAL2(195438990,46) COMP(EQ)
     C                   EVAL      V3=V1*V2*V2A
    MU* VAL1(V4) VAL2(195438990,468) COMP(EQ)
     C                   EVAL      V4=V1*V2*V2A
    MU* VAL1(V3) VAL2(195438990,46) COMP(EQ)
     C                   EVAL(R)   V3=V1*V2*V2A
    MU* VAL1(V4) VAL2(195438990,468652) COMP(EQ)
     C                   EVAL(R)   V4=V1*V2*V2A
    MU* VAL1(V3) VAL2(195438990,47) COMP(EQ)
     C                   EVAL(H)   V3=V1*V2*V2A
    MU* VAL1(V4) VAL2(195438990,468) COMP(EQ)
     C                   EVAL(H)   V4=V1*V2*V2A
    MU* VAL1(V3) VAL2(195438990,47) COMP(EQ)
     C                   EVAL(RH)  V3=V1*V2*V2A
    MU* VAL1(V4) VAL2(195438990,468652) COMP(EQ)
     C                   EVAL(RH)  V4=V1*V2*V2A
    MU* VAL1(V3) VAL2(,40) COMP(EQ)
     C                   EVAL      V3=V1/V2/V2A
    MU* VAL1(V4) VAL2(,406254) COMP(EQ)
     C                   EVAL      V4=V1/V2/V2A
    MU* VAL1(V3) VAL2(,40) COMP(EQ)
     C                   EVAL(R)   V3=V1/V2/V2A
    MU* VAL1(V4) VAL2(,406254) COMP(EQ)
     C                   EVAL(R)   V4=V1/V2/V2A
    MU* VAL1(V3) VAL2(,41) COMP(EQ)
     C                   EVAL(H)   V3=V1/V2/V2A
    MU* VAL1(V4) VAL2(,406254) COMP(EQ)
     C                   EVAL(H)   V4=V1/V2/V2A
    MU* VAL1(V3) VAL2(,41) COMP(EQ)
     C                   EVAL(RH)  V3=V1/V2/V2A
    MU* VAL1(V4) VAL2(,406254) COMP(EQ)
     C                   EVAL(RH)  V4=V1/V2/V2A
      *
     C                   ENDSR
