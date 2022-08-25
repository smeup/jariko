     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 22/08/22  004118  BUSFIO Creazione
     V* 23/08/22  004118  BUSFIO Aggiunto nuovo caso
     V* 25/08/22  004118  BUSFIO Aggiunto nuovo caso di test
     V*=====================================================================
     D*  OBIETTIVO
     D*  Programma finalizzato funzionamento del codice operativo SUBST
     D*
     D* The SUBST operation returns a substring from factor 2, starting at the
     D* location specified in factor 2 for the length specified in factor 1,
     D* and places this substring in the result field.
     V*=====================================================================
     D T               S              2  0                                      Pos iniziale
     D LEN             S              2  0                                      Lunghezza
     D AAA010          S             10                                         Str iniziale 10
     D AAA015          S             15                                         Str iniziale 15
     D RESULT          S             10                                         Risultato
      *---------------------------------------------------------------
     I/COPY QILEGEN,£PDS
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * Substring from factor 2 starting at position 1 for a length of 2.
      * The value placed in RESULT field is 'AB        '.
      * In this case, start position is not specified.
      *
     C                   EVAL      AAA010= 'ABCDEF'
    MU* VAL1(RESULT) VAL2('AB        ') COMP(EQ)
     C     2             SUBST     AAA010        RESULT
      *
      * Substring from factor 2 starting at position 3 for a length of 2.
      * The value placed in RESULT field is 'CD        '.
      *
     C                   EVAL      AAA010= 'ABCDEF'
    MU* VAL1(RESULT) VAL2('CD        ') COMP(EQ)
     C     2             SUBST     AAA010:3      RESULT
      *
      * Substring from factor 2 starting at position 5 for a length of 2.
      * The value placed in RESULT field is 'EF        '.
      *
     C                   EVAL      T = 5
     C                   EVAL      AAA010= 'ABCDEF'
    MU* VAL1(RESULT) VAL2('EF        ') COMP(EQ)
     C     2             SUBST     AAA010:T      RESULT
      *
      * Substring from factor 2 starting at position 5 for a length of 3.
      * The value placed in RESULT field is '123        '.
      * In this case, variables are used to set SUBST.
      *
     C                   EVAL      LEN = 3
     C                   EVAL      T = 5
     C                   EVAL      AAA010 = 'TEST123'
    MU* VAL1(RESULT) VAL2('123        ') COMP(EQ)
     C     LEN           SUBST     AAA010:T      RESULT
      *
      * Substring from factor 2 starting at position 4 for a length of 5.
      * The value placed in RESULT field is 'DEF      '.
      * In this case, the length is greater than the length of the string minus
      * the start position plus 1.
      *
     C                   EVAL      T = 4
     C                   EVAL      AAA010= 'ABCDEF'
    MU* VAL1(RESULT) VAL2('DEF      ') COMP(EQ)
     C     5             SUBST     AAA010:T      RESULT
      *
      * In this SUBST operation, 3 characters are substringed starting
      * at the fifth position of the base string.  Because P is not
      * specified, only the first 3 characters of RESULT are
      * changed.  RESULT contains '123XXXXX'.
      *
     C                   EVAL      LEN = 3
     C                   EVAL      T = 5
     C                   EVAL      AAA010 = 'TEST123'
     C                   EVAL      RESULT = 'XXXXXXXXXX'
    MU* VAL1(RESULT) VAL2('123XXXXXXX') COMP(EQ)
     C     LEN           SUBST     AAA010:T      RESULT
      *
      * This example is the same as the previous one except P
      * specified, and the result is padded with blanks.
      * RESULT equals '123       '.
      *
     C                   EVAL      LEN = 3
     C                   EVAL      T = 5
     C                   EVAL      AAA010 = 'TEST123'
     C                   EVAL      RESULT = 'XXXXXXXXXX'
    MU* VAL1(RESULT) VAL2('123       ') COMP(EQ)
     C     LEN           SUBST(P)  AAA010:T      RESULT
      *
      * Substring from factor 2 starting at position 5 for the rest of length.
      * The value placed in RESULT field is '123ABC    '.
      *
     C                   EVAL      T = 5
     C                   EVAL      AAA015 = 'TEST123ABC'
    MU* VAL1(RESULT) VAL2('123ABC    ') COMP(EQ)
     C                   SUBST     AAA015:T      RESULT
      *
      * Substring from factor 2 starting at position 2 for a length of 1.
      * The value placed in RESULT field is 'A'.
      * In this case, the result variable 'AAA001' is define inline.
      *
     C*                   EVAL      AAA010='1A'
      *MU* VAL1(RESULT) VAL2('A') COMP(EQ)
     C*     1             SUBST(P)  AAA010:2      AAA001            1
      *
      *
     C                   SETON                                        LR
      /COPY QILEGEN,£INZSR
      *---------------------------------------------------------------
     C     £INIZI        BEGSR
     C                   ENDSR
      *---------------------------------------------------------------