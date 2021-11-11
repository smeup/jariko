     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 02/09/21  003123  BUSFIO Creazione
     V*=====================================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle schiere interne al programma:
     D*  schiere con CTDATA vengono associate per nome
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
    MU* VAL1(RESULT) VAL2('PRIMO                             ') COMP(EQ)
     C                   EVAL      RESULT=TXT1(1)
      *
     C                   CLEAR                   RESULT
    MU* VAL1(RESULT) VAL2('THIRD                             ') COMP(EQ)
     C                   EVAL      RESULT=TXT2(3)
      *
     C                   CLEAR                   RESULT
    MU* VAL1(RESULT) VAL2('FIVE                              ') COMP(EQ)
     C                   EVAL      RESULT=TXT3(5)
      *
     C                   CLEAR                   RESULT
    MU* VAL1(RESULT) VAL2('SETTIMO                           ') COMP(EQ)
     C                   EVAL      RESULT=TXT1(7)
      *
     C                   ENDSR
      *---------------------------------------------------------------
**CTDATA TXT2
FIRST
SECOND
THIRD
FOURTH
FIFTH
SIXTH
SEVENTH
EIGHTH
NINTH
**CTDATA TXT3
ONE
TWO
THREE
FOUR
FIVE
SIX
SEVEN
EIGHT
NINE
**CTDATA TXT1
PRIMO
SECONDO
TERZO
QUARTO
QUINTO
SESTO
SETTIMO
OTTAVO
NONO
