   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 21/08/19  001071  BMA Creazione
     V* 22/08/19  001071  BMA Ricompilato
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test su campi di tipo stringa, a lunghezza fissa e varying
     V*
     V*=====================================================================
     D*----------------------------------------------------------------
     D  AAA001         S              1
     D  AAA010         S             10
     D  AAA035         S             35
     D  AAA100         S            100
     D  VAR001         S              1    VARYING
     D  VAR010         S             10    VARYING
     D  VAR035         S             35    VARYING
     D  VAR100         S            100    VARYING
     D  INEQ           S              1N
      *
     D  N1             S              5  0
      *
    MU* VAL1(N1) VAL2(1) COMP(EQ)
     C                   EVAL      N1=%LEN(AAA001)
    MU* VAL1(N1) VAL2(10) COMP(EQ)
     C                   EVAL      N1=%LEN(AAA010)
    MU* VAL1(N1) VAL2(35) COMP(EQ)
     C                   EVAL      N1=%LEN(AAA035)
    MU* VAL1(N1) VAL2(100) COMP(EQ)
     C                   EVAL      N1=%LEN(AAA100)
    MU* VAL1(N1) VAL2(0) COMP(EQ)
     C                   EVAL      N1=%LEN(VAR001)
    MU* VAL1(N1) VAL2(0) COMP(EQ)
     C                   EVAL      N1=%LEN(VAR010)
    MU* VAL1(N1) VAL2(0) COMP(EQ)
     C                   EVAL      N1=%LEN(VAR035)
    MU* VAL1(N1) VAL2(0) COMP(EQ)
     C                   EVAL      N1=%LEN(VAR100)
      *
      * Nell'assegnazione di una stringa, se il valore è più lungo della lunghezza
      * dichiarata il valore viene troncato
    MU* VAL1(AAA001) VAL2('A') COMP(EQ)
     C                   EVAL      AAA001='AAAAAA '
    MU* VAL1(N1) VAL2(1) COMP(EQ)
     C                   EVAL      N1=%LEN(AAA001)
      * Nell'assegnazione di una stringa a lunghezza fissa, se il valore è più corto della lunghezza
      * dichiarata il resto viene riempito con spazi
    MU* VAL1(AAA010) VAL2('AAAAAA') COMP(EQ)
     C                   EVAL      AAA010='AAAAAA '
    MU* VAL1(N1) VAL2(10) COMP(EQ)
     C                   EVAL      N1=%LEN(AAA010)
    MU* VAL1(AAA035) VAL2('AAAAAA') COMP(EQ)
     C                   EVAL      AAA035='AAAAAA '
    MU* VAL1(N1) VAL2(35) COMP(EQ)
     C                   EVAL      N1=%LEN(AAA035)
    MU* VAL1(AAA100) VAL2('AAAAAA') COMP(EQ)
     C                   EVAL      AAA100='AAAAAA '
      * Nel confrontare valori stringa (sia a lunghezza fissa che varying) gli spazi finali
      * non vengono considerati
    MU* VAL1(INEQ) VAL2('1') COMP(EQ)
     C                   EVAL      INEQ=AAA100='AAAAAA'
    MU* VAL1(N1) VAL2(100) COMP(EQ)
     C                   EVAL      N1=%LEN(AAA100)
      * Nell'assegnazione di una stringa, se il valore è più lungo della lunghezza
      * dichiarata il valore viene troncato
    MU* VAL1(VAR001) VAL2('A') COMP(EQ)
     C                   EVAL      VAR001='AAAAAA '
    MU* VAL1(N1) VAL2(1) COMP(EQ)
     C                   EVAL      N1=%LEN(VAR001)
    MU* VAL1(VAR010) VAL2('AAAAAA ') COMP(EQ)
     C                   EVAL      VAR010='AAAAAA '
    MU* VAL1(N1) VAL2(7) COMP(EQ)
     C                   EVAL      N1=%LEN(VAR010)
    MU* VAL1(VAR035) VAL2('AAAAAA ') COMP(EQ)
     C                   EVAL      VAR035='AAAAAA '
    MU* VAL1(N1) VAL2(7) COMP(EQ)
     C                   EVAL      N1=%LEN(VAR035)
    MU* VAL1(VAR100) VAL2('AAAAAA ') COMP(EQ)
     C                   EVAL      VAR100='AAAAAA '
      * Nel valutare la lunghezza di una stringa varying gli spazi finali esplicitati
      * nell'assegnazione vengono conteggiati
    MU* VAL1(N1) VAL2(7) COMP(EQ)
     C                   EVAL      N1=%LEN(VAR100)
      * Nel confrontare valori stringa (sia a lunghezza fissa che varying) gli spazi finali
      * non vengono considerati
    MU* VAL1(INEQ) VAL2('1') COMP(EQ)
     C                   EVAL      INEQ=VAR100='AAAAAA'
      *
     C                   SETON                                        LR
