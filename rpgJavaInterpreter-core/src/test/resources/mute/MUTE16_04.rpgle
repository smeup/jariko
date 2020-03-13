     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 13/03/20          LOMFRN Created
     V*=====================================================================
     V*  Test program for LOOKUP op code - Testing for equality
     V*=====================================================================
     D Msg             S             50
     D  ARRAY          S              1    DIM(10)
     D N               S              2  0
     C                   EVAL      ARRAY(1)='B'
     C                   EVAL      ARRAY(2)='C'
     C                   EVAL      ARRAY(3)='D'
     C                   EVAL      ARRAY(4)='G'
     C                   EVAL      ARRAY(5)='H'
     C                   EVAL      ARRAY(6)='I'
     C                   EVAL      ARRAY(7)='A'
     C                   EVAL      ARRAY(8)='B'
     C                   EVAL      ARRAY(9)='D'
     C                   EVAL      ARRAY(10)='Z'
      *---------------------------------------------------------------------
     C                   EVAL      N = 1
     C                   SETOFF                                           69
     C     'Z'           LOOKUP    ARRAY(N)                               69
    MU* VAL1(N) VAL2(10) COMP(EQ)
    MU* VAL1(*IN69) VAL2(*ON) COMP(EQ)
     C   69'69 On'       DSPLY
     C     N             DSPLY
      *---------------------------------------------------------------------
     C                   EVAL      N = 1
     C                   SETOFF                                           69
     C     'D'           LOOKUP    ARRAY(N)                               69
    MU* VAL1(N) VAL2(3) COMP(EQ)
    MU* VAL1(*IN69) VAL2(*ON) COMP(EQ)
     C   69'69 On'       DSPLY
     C     N             DSPLY
      *---------------------------------------------------------------------
     C                   EVAL      N = 4
     C                   SETOFF                                           69
     C     'D'           LOOKUP    ARRAY(N)                               69
    MU* VAL1(N) VAL2(9) COMP(EQ)
    MU* VAL1(*IN69) VAL2(*ON) COMP(EQ)
     C   69'69 On'       DSPLY
     C     N             DSPLY
      *---------------------------------------------------------------------
     C                   EVAL      N = 2
     C                   SETOFF                                           69
     C     'Q'           LOOKUP    ARRAY(N)                               69
    MU* VAL1(N) VAL2(1) COMP(EQ)
    MU* VAL1(*IN69) VAL2(*OFF) COMP(EQ)
     C   69'69 On'       DSPLY
     C     N             DSPLY
      *---------------------------------------------------------------------
     C                   SETON                                        LR
