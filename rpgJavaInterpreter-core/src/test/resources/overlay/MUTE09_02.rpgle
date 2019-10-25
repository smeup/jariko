   COP* *NOUI
      * DECEDIT('.') indica che il separatore dei decimali è il punto
      *              (e che quindi il separatore delle migliaia è la virgola)
      *              e in caso di numero inferiore a 1 lo 0 non è mostrato (es.  .12400)
      * DECEDIT(',') indica che il separatore dei decimali è la virgola
      *              (e che quindi il separatore delle migliaia è il punto)
      *              e in caso di numero inferiore a 1 lo 0 non è mostrato (es.  ,12400)
      * DECEDIT('0.') indica che il separatore dei decimali è il punto
      *              (e che quindi il separatore delle migliaia è la virgola)
      *              e in caso di numero inferiore a 1 lo 0 è mostrato (es. 0.12400)
      * DECEDIT('0,') indica che il separatore dei decimali è la virgola
      *              (e che quindi il separatore delle migliaia è il punto)
      *              e in caso di numero inferiore a 1 lo 0 è mostrato (es. 0,12400)
      * DECEDIT(*JOBRUN) indica che il separatore dei decimali e delle migliaia vengono determinati
      *              a runtime in base ad una variabile di ambiente del job.
      * Se non indicato nulla assume DECEDIT('.').
     H DECEDIT('.')
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 17/06/19  000908  CM Creato
     V* 19/06/19  V5R1    BMA Check-out 000908 in SMEDEV
     V* 14/08/19  001059  BMA Esplicitato DECEDIT
     V* 19/08/19  V5R1    CM Check-out 001059 in SMEDEV
     V* 22/08/19  001071  BMA Migliorati esempi
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
      **
      **   NUMERO NEGATIVO CON CIFRE DECIMALI
      **
      * Quando si assegna direttamente un valore ad un campo numerico,
      * si può utilizzare indifferentemente come separatore decimale il punto o la virgola
      * (mentre non va indicato il separatore delle migliaia).
    MU* VAL1(V1) VAL2(-9010,54897) COMP(EQ)
     C                   EVAL      V1=-9010,54897
      * Il numero di decimali (la scala) è importante sia per questioni di arrotondamenti in
      * operazioni di moltiplicazione o divisione, sia per la corretta editazione di un numero.
      * Il numero totale di cifre (la precisione) va considerata in quanto le cifre intere vanno
      * valorizzate con spazio o 0 a seconda dell'editcode
      * '1' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: no
    MU* VAL1(S1) VAL2('9,010.548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'1')
      * '2' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: no
    MU* VAL1(S1) VAL2('9,010.548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'2')
      * '3' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: no
    MU* VAL1(S1) VAL2('9010.548970         ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'3')
      * '4' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: no
    MU* VAL1(S1) VAL2('9010.548970         ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'4')
      * 'A' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: CR
    MU* VAL1(S1) VAL2('9,010.548970CR      ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'A')
      * 'B' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: CR
    MU* VAL1(S1) VAL2('9,010.548970CR      ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'B')
      * 'C' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: CR
    MU* VAL1(S1) VAL2('9010.548970CR       ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'C')
      * 'D' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: CR
    MU* VAL1(S1) VAL2('9010.548970CR       ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'D')
      * 'J' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: a destra
    MU* VAL1(S1) VAL2('9,010.548970-       ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'J')
      * 'K' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: a destra
    MU* VAL1(S1) VAL2('9,010.548970-       ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'K')
      * 'L' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: a destra
    MU* VAL1(S1) VAL2('9010.548970-        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'L')
      * 'M' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: a destra
    MU* VAL1(S1) VAL2('9010.548970-        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'M')
      * 'N' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: a sinistra
    MU* VAL1(S1) VAL2('-9,010.548970       ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'N')
      * 'O' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: a sinistra
    MU* VAL1(S1) VAL2('-9,010.548970       ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'O')
      * 'P' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: a sinistra
    MU* VAL1(S1) VAL2('-9010.548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'P')
      * 'Q' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: a sinistra
    MU* VAL1(S1) VAL2('-9010.548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'Q')
      * 'X' : Separatore migliaia: no
      *       separatore decimale: no
      *       segno: no
    MU* VAL1(S1) VAL2('9010548970          ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'X')
      * 'Z' : Separatore migliaia: no
      *       separatore decimale: no
      *       segno: no
    MU* VAL1(S1) VAL2('9010548970          ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'Z')
      **
      **   NUMERO POSITIVO CON CIFRE DECIMALI
      **
    MU* VAL1(V1) VAL2(9010,54897) COMP(EQ)
     C                   EVAL      V1=9010,54897
      * Il numero di decimali (la scala) è importante sia per questioni di arrotondamenti in
      * operazioni di moltiplicazione o divisione, sia per la corretta editazione di un numero.
      * Il numero totale di cifre (la precisione) va considerata in particolare nell'editazione
      * con editcode 'X'
      * '1' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: no
    MU* VAL1(S1) VAL2('9,010.548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'1')
      * '2' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: no
    MU* VAL1(S1) VAL2('9,010.548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'2')
      * '3' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: no
    MU* VAL1(S1) VAL2('9010.548970         ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'3')
      * '4' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: no
    MU* VAL1(S1) VAL2('9010.548970         ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'4')
      * 'A' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: CR
    MU* VAL1(S1) VAL2('9,010.548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'A')
      * 'B' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: CR
    MU* VAL1(S1) VAL2('9,010.548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'B')
      * 'C' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: CR
    MU* VAL1(S1) VAL2('9010.548970         ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'C')
      * 'D' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: CR
    MU* VAL1(S1) VAL2('9010.548970         ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'D')
      * 'J' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: a destra
    MU* VAL1(S1) VAL2('9,010.548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'J')
      * 'K' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: a destra
    MU* VAL1(S1) VAL2('9,010.548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'K')
      * 'L' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: a destra
    MU* VAL1(S1) VAL2('9010.548970         ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'L')
      * 'M' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: a destra
    MU* VAL1(S1) VAL2('9010.548970         ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'M')
      * 'N' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: a sinistra
    MU* VAL1(S1) VAL2(' 9,010.548970       ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'N')
      * 'O' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: a sinistra
    MU* VAL1(S1) VAL2(' 9,010.548970       ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'O')
      * 'P' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: a sinistra
    MU* VAL1(S1) VAL2(' 9010.548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'P')
      * 'Q' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: a sinistra
    MU* VAL1(S1) VAL2(' 9010.548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'Q')
      * 'X' : Separatore migliaia: no
      *       separatore decimale: no
      *       segno: no
    MU* VAL1(S1) VAL2('9010548970          ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'X')
      * 'Z' : Separatore migliaia: no
      *       separatore decimale: no
      *       segno: no
    MU* VAL1(S1) VAL2('9010548970          ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'Z')
      **
      **   NUMERO POSITIVO CON CIFRE DECIMALI E VALORE INFERIORE A 1
      **
    MU* VAL1(V1) VAL2(0,54897) COMP(EQ)
     C                   EVAL      V1=0,54897
      * Il numero di decimali (la scala) è importante sia per questioni di arrotondamenti in
      * operazioni di moltiplicazione o divisione, sia per la corretta editazione di un numero.
      * Il numero totale di cifre (la precisione) va considerata in particolare nell'editazione
      * con editcode 'X'
      * '1' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: no
    MU* VAL1(S1) VAL2('     .548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'1')
      * '2' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: no
    MU* VAL1(S1) VAL2('     .548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'2')
      * '3' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: no
    MU* VAL1(S1) VAL2('    .548970         ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'3')
      * '4' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: no
    MU* VAL1(S1) VAL2('    .548970         ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'4')
      * 'A' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: CR
    MU* VAL1(S1) VAL2('     .548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'A')
      * 'B' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: CR
    MU* VAL1(S1) VAL2('     .548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'B')
      * 'C' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: CR
    MU* VAL1(S1) VAL2('    .548970         ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'C')
      * 'D' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: CR
    MU* VAL1(S1) VAL2('    .548970         ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'D')
      * 'J' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: a destra
    MU* VAL1(S1) VAL2('     .548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'J')
      * 'K' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: a destra
    MU* VAL1(S1) VAL2('     .548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'K')
      * 'L' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: a destra
    MU* VAL1(S1) VAL2('    .548970         ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'L')
      * 'M' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: a destra
    MU* VAL1(S1) VAL2('    .548970         ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'M')
      * 'N' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: a sinistra
    MU* VAL1(S1) VAL2('      .548970       ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'N')
      * 'O' : Separatore migliaia: sì
      *       separatore decimale: sì
      *       segno: a sinistra
    MU* VAL1(S1) VAL2('      .548970       ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'O')
      * 'P' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: a sinistra
    MU* VAL1(S1) VAL2('     .548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'P')
      * 'Q' : Separatore migliaia: no
      *       separatore decimale: sì
      *       segno: a sinistra
    MU* VAL1(S1) VAL2('     .548970        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'Q')
      * 'X' : Separatore migliaia: no
      *       separatore decimale: no
      *       segno: no
    MU* VAL1(S1) VAL2('0000548970          ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'X')
      * 'Z' : Separatore migliaia: no
      *       separatore decimale: no
      *       segno: no
    MU* VAL1(S1) VAL2('    548970          ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'Z')
      **
      **   NUMERO INTERO NEGATIVO
      **
    MU* VAL1(V2) VAL2(-1234) COMP(EQ)
     C                   EVAL      V2=-1234
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
     C                   EVAL      S1=%EDITC(V2:'X')
    MU* VAL1(S1) VAL2('1234                ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'Z')
      **
      **   NUMERO INTERO POSITIVO
      **
    MU* VAL1(V2) VAL2(234) COMP(EQ)
     C                   EVAL      V2=234
    MU* VAL1(S1) VAL2('  234               ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'1')
    MU* VAL1(S1) VAL2('  234               ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'2')
    MU* VAL1(S1) VAL2(' 234                ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'3')
    MU* VAL1(S1) VAL2(' 234                ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'4')
    MU* VAL1(S1) VAL2('  234               ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'A')
    MU* VAL1(S1) VAL2('  234               ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'B')
    MU* VAL1(S1) VAL2(' 234               ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'C')
    MU* VAL1(S1) VAL2(' 234                ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'D')
    MU* VAL1(S1) VAL2('  234               ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'J')
    MU* VAL1(S1) VAL2('  234               ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'K')
    MU* VAL1(S1) VAL2(' 234                ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'L')
    MU* VAL1(S1) VAL2(' 234                ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'M')
    MU* VAL1(S1) VAL2('   234              ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'N')
    MU* VAL1(S1) VAL2('   234              ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'O')
    MU* VAL1(S1) VAL2('  234               ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'P')
    MU* VAL1(S1) VAL2('  234               ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'Q')
    MU* VAL1(S1) VAL2('0234                ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'X')
    MU* VAL1(S1) VAL2(' 234                ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V2:'Z')
      **
      **   NUMERO INTERO A 6 CIFRE USATO COME DATA
      **
    MU* VAL1(D2) VAL2(181019) COMP(EQ)
     C                   EVAL      D2=181019
    MU* VAL1(S1) VAL2('18/10/19            ') COMP(EQ)
     C                   EVAL      S1=%EDITC(D2:'Y')
      **
      **   NUMERO INTERO A 8 CIFRE USATO COME DATA
      **
    MU* VAL1(D1) VAL2(18102019) COMP(EQ)
     C                   EVAL      D1=18102019
    MU* VAL1(S1) VAL2('18/10/2019          ') COMP(EQ)
     C                   EVAL      S1=%EDITC(D1:'Y')
      *
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
    MU* VAL1(S1) VAL2('     .000000        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'B')
    MU* VAL1(S1) VAL2('    .000000        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'C')
    MU* VAL1(S1) VAL2('    .000000        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'D')
    MU* VAL1(S1) VAL2('     .000000        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'J')
    MU* VAL1(S1) VAL2('                    ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'K')
    MU* VAL1(S1) VAL2('    .000000        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'L')
    MU* VAL1(S1) VAL2('                    ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'M')
    MU* VAL1(S1) VAL2('      .000000        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'N')
    MU* VAL1(S1) VAL2('                    ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'O')
    MU* VAL1(S1) VAL2('     .000000        ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'P')
    MU* VAL1(S1) VAL2('                    ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'Q')
    MU* VAL1(S1) VAL2('                    ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'Z')
    MU* VAL1(S1) VAL2('0000000000          ') COMP(EQ)
     C                   EVAL      S1=%EDITC(V1:'X')
    MU* VAL1(S1) VAL2(' 0/00/0000          ') COMP(EQ)
     C                   EVAL      S1=%EDITC(D1:'Y')
    MU* VAL1(S1) VAL2(' 0/00/00            ') COMP(EQ)
     C                   EVAL      S1=%EDITC(D2:'Y')
     C                   ENDSR
