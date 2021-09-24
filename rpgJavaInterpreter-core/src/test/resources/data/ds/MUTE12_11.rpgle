     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 17/08/21  003123  BERNI Creato
     V*=====================================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle schiere interne al programma
     V*=====================================================================
     DRESULT           S             35
     D TXT1            S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
     D TXT2            S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
     D TXT3            S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
     C                   EXSR      ARRAY
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
     C     ARRAY         BEGSR
      * Test sullla prima schiera
     C                   CLEAR                   RESULT
    MU* VAL1(RESULT) VAL2('FIRST                             ') COMP(EQ)
     C                   EVAL      RESULT=TXT1(1)
      *
     C                   CLEAR                   RESULT
    MU* VAL1(RESULT) VAL2('THIRD                             ') COMP(EQ)
     C                   EVAL      RESULT=TXT1(3)
      *
     C                   CLEAR                   RESULT
    MU* VAL1(RESULT) VAL2('FIFTH                             ') COMP(EQ)
     C                   EVAL      RESULT=TXT1(5)
      *
     C                   CLEAR                   RESULT
    MU* VAL1(RESULT) VAL2('SEVENTH                           ') COMP(EQ)
     C                   EVAL      RESULT=TXT1(7)
      *
      * Test sulla seconda schiera
     C                   CLEAR                   RESULT
    MU* VAL1(RESULT) VAL2('ONE                               ') COMP(EQ)
     C                   EVAL      RESULT=TXT2(1)
      *
     C                   CLEAR                   RESULT
    MU* VAL1(RESULT) VAL2('THREE                             ') COMP(EQ)
     C                   EVAL      RESULT=TXT2(3)
      *
     C                   CLEAR                   RESULT
    MU* VAL1(RESULT) VAL2('FIVE                              ') COMP(EQ)
     C                   EVAL      RESULT=TXT2(5)
      *
     C                   CLEAR                   RESULT
    MU* VAL1(RESULT) VAL2('SEVEN                             ') COMP(EQ)
     C                   EVAL      RESULT=TXT2(7)
      *
      * Test sulla terza schiera
     C                   CLEAR                   RESULT
    MU* VAL1(RESULT) VAL2('PRIMO                             ') COMP(EQ)
     C                   EVAL      RESULT=TXT3(1)
      *
     C                   CLEAR                   RESULT
    MU* VAL1(RESULT) VAL2('TERZO                             ') COMP(EQ)
     C                   EVAL      RESULT=TXT3(3)
      *
     C                   CLEAR                   RESULT
    MU* VAL1(RESULT) VAL2('QUINTO                            ') COMP(EQ)
     C                   EVAL      RESULT=TXT3(5)
      *
     C                   CLEAR                   RESULT
    MU* VAL1(RESULT) VAL2('SETTIMO                           ') COMP(EQ)
     C                   EVAL      RESULT=TXT3(7)
      *
     C                   ENDSR
      *---------------------------------------------------------------
**TXT1
FIRST
SECOND
THIRD
FOURTH
FIFTH
SIXTH
SEVENTH
EIGHTH
NINTH
** TXT2
ONE
TWO
THREE
FOUR
FIVE
SIX
SEVEN
EIGHT
NINE
** TXT3
PRIMO
SECONDO
TERZO
QUARTO
QUINTO
SESTO
SETTIMO
OTTAVO
NONO