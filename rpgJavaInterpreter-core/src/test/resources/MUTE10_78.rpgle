     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 22/08/22  004118  BUSFIO Creazione
     V* 23/08/22  004118  BUSFIO Aggiunto nuovo caso
     V*=====================================================================
     D*  OBIETTIVO
     D*  Programma finalizzato funzionamento del codice operativo SUBST
     D*
     D* The SUBST operation returns a substring from factor 2, starting at the
     D* location specified in factor 2 for the length specified in factor 1,
     D* and places this substring in the result field.
     V*=====================================================================
      *
     D T               S              2  0                                      Pos iniziale
     D LEN             S              2  0                                      Lunghezza
     D STRING          S             10                                         Str iniziale
     D RESULT          S             10                                         Risultato
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * The SUBST operation extracts the substring from factor 2 starting
      * at position 1 for a length of 2.  The value 'AB' is placed in the
      * result field TARGET.
     C                   EVAL      STRING= 'ABCDEF'
    MU* VAL1(RESULT) VAL2('AB        ') COMP(EQ)
     C     2             SUBST     STRING        RESULT
     C     RESULT        DSPLY
      *
      *
      *
     C                   EVAL      STRING= 'ABCDEF'
    MU* VAL1(RESULT) VAL2('CD        ') COMP(EQ)
     C     2             SUBST     STRING:3      RESULT
     C     RESULT        DSPLY
      * The SUBST operation extracts the substring from factor 2 starting
      * at position 3 for a length of 2.  The value 'CD' is placed in the
      * result field TARGET.
     C                   EVAL      T = 5
     C                   EVAL      STRING= 'ABCDEF'
    MU* VAL1(RESULT) VAL2('EF        ') COMP(EQ)
     C     2             SUBST     STRING:T      RESULT
     C     RESULT        DSPLY
      *
      *
      *
     C                   EVAL      LEN = 3
     C                   EVAL      T = 5
     C                   EVAL      STRING = 'TEST123'
    MU* VAL1(RESULT) VAL2('123        ') COMP(EQ)
     C     LEN           SUBST     STRING:T      RESULT
     C     RESULT        DSPLY
      * In this SUBST operation, the length is greater than the length
      * of the string minus the start position plus 1.  As a result,
      * indicator 90 is set on and the result field is not changed.
     C                   EVAL      T = 4
     C                   EVAL      STRING= 'ABCDEF'
    MU* VAL1(RESULT) VAL2('DEF      ') COMP(EQ)
     C     5             SUBST     STRING:T      RESULT
     C     RESULT        DSPLY
      *
      * In this SUBST operation, 3 characters are substringed starting
      * at the fifth position of the base string.  Because P is not
      * specified, only the first 3 characters of TARGET are
      * changed.  TARGET contains '123XXXXX'.
     C                   EVAL      LEN = 3
     C                   EVAL      T = 5
     C                   EVAL      STRING = 'TEST123'
     C                   EVAL      RESULT = 'XXXXXXXXXX'
    MU* VAL1(RESULT) VAL2('123XXXXXXX') COMP(EQ)
     C     LEN           SUBST     STRING:T      RESULT
     C     RESULT        DSPLY
      *
      * This example is the same as the previous one except P
      * specified, and the result is padded with blanks.
      * TARGET equals '123       '.
     C                   EVAL      LEN = 3
     C                   EVAL      T = 5
     C                   EVAL      STRING = 'TEST123'
     C                   EVAL      RESULT = 'XXXXXXXXXX'
    MU* VAL1(RESULT) VAL2('123       ') COMP(EQ)
     C     LEN           SUBST(P)  STRING:T      RESULT
     C     RESULT        DSPLY
      *
      *
     C                   EVAL      STRING='1A'
    MU* VAL1(RESULT) VAL2('A') COMP(EQ)
     C*     1             SUBST(P)  STRING:2      AAA001            1
     C*     AAA001        DSPLY
      *
      *
     C                   SETON                                        LR