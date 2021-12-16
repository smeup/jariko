     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 17/08/21  003123  BERNI Creato
     V* 18/08/21  003123  BERNI Aggiunti casi
     V*=====================================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulla %LOOKUP
     V*=====================================================================
     D  ARRAY          S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
     D  INDEX          S              2  0
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
     C                   EXSR      ROUTINE
     C                   SETON                                        LR
      *---------------------------------------------------------------
     D* Routine
      *---------------------------------------------------------------
     C     ROUTINE       BEGSR
      *
    MU* VAL1(INDEX) VAL2(1) COMP(EQ)
     C                   EVAL      INDEX=%LOOKUP('AAAAA':ARRAY:1:3)
      *
    MU* VAL1(INDEX) VAL2(2) COMP(EQ)
     C                   EVAL      INDEX=%LOOKUP('BBBBB':ARRAY:1:3)
      *
    MU* VAL1(INDEX) VAL2(2) COMP(EQ)
     C                   EVAL      INDEX=%LOOKUP('BBBBB':ARRAY:2:2)
      *
    MU* VAL1(INDEX) VAL2(9) COMP(EQ)
     C                   EVAL      INDEX=%LOOKUP('IIIII':ARRAY:9:1)
      *
    MU* VAL1(INDEX) VAL2(0) COMP(EQ)
     C                   EVAL      INDEX=%LOOKUP('AAAAA':ARRAY:7)
      *
    MU* VAL1(INDEX) VAL2(0) COMP(EQ)
     C                   EVAL      INDEX=%LOOKUP('IIIII':ARRAY:1:3)
      *
    MU* VAL1(INDEX) VAL2(2) COMP(EQ)
     C                   EVAL      INDEX=%LOOKUP('BBBBB':ARRAY:2)
      *
     C                   ENDSR
      *---------------------------------------------------------------
** TXT1
AAAAA
BBBBB
CCCCC
DDDDD
EEEEE
FFFFF
GGGGG
HHHHH
IIIII