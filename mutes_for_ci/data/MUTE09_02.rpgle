   COP* *NOUI
     H DECEDIT('.')
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 17/06/19  000908  CM Creato
     V* 19/06/19  V5R1    BMA Check-out 000908 in SMEDEV
     V* 14/08/19  001059  BMA Esplicitato DECEDIT
     V*=====================================================================
     D* OBIETTIVO
     D*  Programma finalizzato ai test sulle editazioni
     V*---------------------------------------------------------------------
      * Considerare i seguenti codici operativi
      *+----------+--+---------!--+
      *!RPGLE     !ST!BUILT-IN !ST!
      *+-------------+ --------!--+
      *!EVAL      !  !%EDITC   !  !
      *+----------+--+---------+--+
     D V1              S             10  6
     D V2              S              4  0
     D D1              S              8  0
     D D2              S              6  0
     D S1              S             20
     C                   EXSR      F_EDITC
      *
    MU* Type="NOXMI"
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test EDITC
      *---------------------------------------------------------------------
     C     F_EDITC       BEGSR
    MU* VAL1(V1) VAL2(9010,54897) COMP(EQ)
     C                   EVAL      V1=-9010,54897
    MU* VAL1(V2) VAL2(1234) COMP(EQ)
     C                   EVAL      V2=-1234
    MU* VAL1(D2) VAL2(181019) COMP(EQ)
     C                   EVAL      D2=181019
    MU* VAL1(D1) VAL2(18102019) COMP(EQ)
     C                   EVAL      D1=18102019
      *
    MU* VAL1(S1) VAL2('9,010.548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'1')
    MU* VAL1(S1) VAL2('9,010.548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'2')
    MU* VAL1(S1) VAL2('9010.548970         ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'3')
    MU* VAL1(S1) VAL2('9010.548970         ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'4')
    MU* VAL1(S1) VAL2('9,010.548970CR      ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'A')
    MU* VAL1(S1) VAL2('9,010.548970CR      ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'B')
    MU* VAL1(S1) VAL2('9010.548970CR       ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'C')
    MU* VAL1(S1) VAL2('9010.548970CR       ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'D')
    MU* VAL1(S1) VAL2('9,010.548970-       ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'J')
    MU* VAL1(S1) VAL2('9,010.548970-       ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'K')
    MU* VAL1(S1) VAL2('9010.548970-        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'L')
    MU* VAL1(S1) VAL2('9010.548970-        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'M')
    MU* VAL1(S1) VAL2('-9,010.548970       ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'N')
    MU* VAL1(S1) VAL2('-9,010.548970       ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'O')
    MU* VAL1(S1) VAL2('-9010.548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'P')
    MU* VAL1(S1) VAL2('-9010.548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'Q')
    MU* VAL1(S1) VAL2('9010548970          ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'Z')
    MU* VAL1(S1) VAL2('18/10/2019          ') COMP(EQ)
     C                   EVAL      S1=%EDITC(D1:'Y')
    MU* VAL1(S1) VAL2('1,234               ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'1')
    MU* VAL1(S1) VAL2('1,234               ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'2')
    MU* VAL1(S1) VAL2('1234                ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'3')
    MU* VAL1(S1) VAL2('1234                ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'4')
    MU* VAL1(S1) VAL2('1,234CR             ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'A')
    MU* VAL1(S1) VAL2('1,234CR             ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'B')
    MU* VAL1(S1) VAL2('1234CR             ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'C')
    MU* VAL1(S1) VAL2('1234CR              ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'D')
    MU* VAL1(S1) VAL2('1,234-              ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'J')
    MU* VAL1(S1) VAL2('1,234-              ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'K')
    MU* VAL1(S1) VAL2('1234-               ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'L')
    MU* VAL1(S1) VAL2('1234-               ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'M')
    MU* VAL1(S1) VAL2('-1,234              ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'N')
    MU* VAL1(S1) VAL2('-1,234              ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'O')
    MU* VAL1(S1) VAL2('-1234               ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'P')
    MU* VAL1(S1) VAL2('-1234               ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'Q')
    MU* VAL1(S1) VAL2('1234                ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'Z')
    MU* VAL1(S1) VAL2('18/10/19            ') COMP(EQ)
     C                   EVAL      S1=%EDITC(D2:'Y')
      *
    MU* VAL1(V1) VAL2(0) COMP(EQ)
     C                   EVAL      V1=0
    MU* VAL1(D1) VAL2(0) COMP(EQ)
     C                   EVAL      D1=0
    MU* VAL1(D2) VAL2(0) COMP(EQ)
     C                   EVAL      D2=0
    MU* VAL1(S1) VAL2('     .000000        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'1')
    MU* VAL1(S1) VAL2('                    ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'2')
    MU* VAL1(S1) VAL2('    .000000         ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'3')
    MU* VAL1(S1) VAL2('                    ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'4')
    MU* VAL1(S1) VAL2('     .000000        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'A')
    MU* VAL1(S1) VAL2('                    ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'B')
    MU* VAL1(S1) VAL2('     .000000        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'C')
    MU* VAL1(S1) VAL2('                    ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'D')
    MU* VAL1(S1) VAL2('     .000000        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'J')
    MU* VAL1(S1) VAL2('                    ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'K')
    MU* VAL1(S1) VAL2('     .000000        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'L')
    MU* VAL1(S1) VAL2('                    ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'M')
    MU* VAL1(S1) VAL2('     .000000        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'N')
    MU* VAL1(S1) VAL2('                    ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'O')
    MU* VAL1(S1) VAL2('     .000000        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'P')
    MU* VAL1(S1) VAL2('                    ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'Q')
    MU* VAL1(S1) VAL2('                    ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'Z')
    MU* VAL1(S1) VAL2(' 0/00/0000          ') COMP(EQ)
     C                   EVAL      S1=%EDITC(D1:'Y')
    MU* VAL1(S1) VAL2(' 0/00/00            ') COMP(EQ)
     C                   EVAL      S1=%EDITC(D2:'Y')
     C                   ENDSR
