     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 24/08/22  004118  BUSFIO Creazione
     V*=====================================================================
     D*  OBIETTIVO
     D*  Programma finalizzato funzionamento del codice operativo MOVE
     D*
     V*=====================================================================
      *
     D AAA001          S              1
     D AAA003          S              3
     D AAA005          S              5
     D AAA006          S              6
     D AAA008          S              8
     D ARR             S              5    DIM(2)
     D RESULT          S             10                                         Risultato
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      *** MOVE - MOVE(P)
     C                   EVAL      AAA006= 'ABCDEF'
    MU* VAL1(RESULT) VAL2('    ABCDEF') COMP(EQ)
     C                   MOVE      AAA006        RESULT
      *
     C*                   EVAL      AAA006= 'ABCDEF'
     C*                   EVAL      RESULT = 'XXXXXXXXXX'
      *MU* VAL1(RESULT) VAL2('    ABCDEF') COMP(EQ)
     C*                   MOVE(P)   AAA006        RESULT
      *
      *
      *** MOVEL - MOVEL(P)
     C                   EVAL      RESULT = ' '
     C                   EVAL      AAA003= '123'
    MU* VAL1(RESULT) VAL2('123       ') COMP(EQ)
     C                   MOVEL     AAA003        RESULT
      *
     C                   EVAL      AAA003 = '123'
     C                   EVAL      RESULT = 'XXXXXXXXXX'
    MU* VAL1(RESULT) VAL2('123XXXXXXX') COMP(EQ)
     C                   MOVEL     AAA003        RESULT
      *
     C                   EVAL      AAA008 = 'ABCDEFGH'
     C                   EVAL      RESULT = 'XXXXXXXXXX'
    MU* VAL1(RESULT) VAL2('ABCDEFGH  ') COMP(EQ)
     C                   MOVEL(P)  AAA008        RESULT
      *
     C                   EVAL      AAA001 = 'A'
    MU* VAL1(RESULT) VAL2('A         ') COMP(EQ)
     C                   MOVEL(P)  AAA001        RESULT
     C                   EVAL      AAA003 = 'XYZ'
    MU* VAL1(RESULT) VAL2('A      XYZ') COMP(EQ)
     C                   MOVE      AAA003        RESULT
      *** MOVEA - MOVEA(P)
     C                   EVAL      ARR(1) = '1'
     C                   EVAL      ARR(2) = '2'
    MU* VAL1(RESULT) VAL2('1    2    ') COMP(EQ)
     C                   MOVEA     ARR(1)        RESULT
      *
     C                   EVAL      AAA005 = 'PROVA'
    MU* VAL1(RESULT) VAL2('2         ') COMP(EQ)
     C                   MOVEA(P)  ARR(2)        RESULT
    MU* VAL1(RESULT) VAL2('2    PROVA') COMP(EQ)
     C                   MOVE      AAA005        RESULT
      *
      *
     C                   SETON                                        LR