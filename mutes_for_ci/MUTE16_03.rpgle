     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 12/03/20  001676  FP Creato
     V* 12/03/20  V5R1    BMA Check-out 001676 in SMEDEV
     V*=====================================================================
     D  ARRAY          S              1    DIM(10) ASCEND
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
     C                   EVAL      ARRAY(1)='B'
     C                   EVAL      ARRAY(2)='C'
     C                   EVAL      ARRAY(3)='D'
     C                   EVAL      ARRAY(4)='G'
     C                   EVAL      ARRAY(5)='H'
     C                   EVAL      ARRAY(6)='I'
     C                   EVAL      ARRAY(7)='A'
     C                   EVAL      ARRAY(8)='B'
     C                   EVAL      ARRAY(9)='D'
      * EQUAL CASE
     C                   SETOFF                                           22
      * Search from index 0
    MU* VAL1(*IN22) VAL2('1') COMP(EQ)
     C     'A'           LOOKUP    ARRAY                                  22
      *
     C                   SETOFF                                           22
      * Search from index 0
    MU* VAL1(*IN22) VAL2('0') COMP(EQ)
     C     'Z'           LOOKUP    ARRAY                                  22
      *
     C                   SETOFF                                           22
      * Search from index 8
    MU* VAL1(*IN22) VAL2('0') COMP(EQ)
     C     'A'           LOOKUP    ARRAY(8)                               22
      *
     C                   SETOFF                                           22
      * Search from index 4
    MU* VAL1(*IN22) VAL2('0') COMP(EQ)
     C     'C'           LOOKUP    ARRAY(4)                               22
      *
      * LOWER CASE
     C                   SETOFF                                         2122
      * Search from index 0
    MU* VAL1(*IN21) VAL2('1') COMP(EQ)
     C     'J'           LOOKUP    ARRAY                                2122
    MU* VAL1(*IN22) VAL2('0') COMP(EQ)
     C     'J'           LOOKUP    ARRAY                                2122
      *
     C                   SETOFF                                         2122
      * Search from index 8
    MU* VAL1(*IN21) VAL2('1') COMP(EQ)
     C     'J'           LOOKUP    ARRAY(8)                             2122
    MU* VAL1(*IN22) VAL2('0') COMP(EQ)
     C     'J'           LOOKUP    ARRAY(8)                             2122
      *
     C                   SETOFF                                         2122
      * Search from index 8
    MU* VAL1(*IN21) VAL2('1') COMP(EQ)
     C     'I'           LOOKUP    ARRAY(8)                             2122
    MU* VAL1(*IN22) VAL2('0') COMP(EQ)
     C     'I'           LOOKUP    ARRAY(8)                             2122
      *
     C                   SETOFF                                         2122
      * Search from index 6
    MU* VAL1(*IN21) VAL2('0') COMP(EQ)
     C     'A'           LOOKUP    ARRAY(6)                             2122
    MU* VAL1(*IN22) VAL2('0') COMP(EQ)
     C     'A'           LOOKUP    ARRAY(6)                             2122
      *
      * GREATER CASE
     C                   SETOFF                                       20  22
      * Search from index 0
    MU* VAL1(*IN20) VAL2('1') COMP(EQ)
     C     'E'           LOOKUP    ARRAY                              20  22
    MU* VAL1(*IN22) VAL2('0') COMP(EQ)
     C     'E'           LOOKUP    ARRAY                              20  22
      *
     C                   SETOFF                                       20  22
      * Search from index 8
    MU* VAL1(*IN20) VAL2('0') COMP(EQ)
     C     'J'           LOOKUP    ARRAY(8)                           20  22
    MU* VAL1(*IN22) VAL2('0') COMP(EQ)
     C     'J'           LOOKUP    ARRAY(8)                           20  22
      *
     C                   SETOFF                                       20  22
      * Search from index 3
    MU* VAL1(*IN20) VAL2('1') COMP(EQ)
     C     'E'           LOOKUP    ARRAY(3)                           20  22
    MU* VAL1(*IN22) VAL2('0') COMP(EQ)
     C     'E'           LOOKUP    ARRAY(3)                           20  22
      *
     C                   SETOFF                                       20  22
      * Search from index 9
    MU* VAL1(*IN20) VAL2('0') COMP(EQ)
     C     'I'           LOOKUP    ARRAY(9)                           20  22
    MU* VAL1(*IN22) VAL2('0') COMP(EQ)
     C     'I'           LOOKUP    ARRAY(9)                           20  22
      *
     C                   SETOFF                                       20  22
      * Search from index 7
    MU* VAL1(*IN20) VAL2('0') COMP(EQ)
     C     'A'           LOOKUP    ARRAY(7)                           20  22
    MU* VAL1(*IN22) VAL2('1') COMP(EQ)
     C     'A'           LOOKUP    ARRAY(7)                           20  22
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
