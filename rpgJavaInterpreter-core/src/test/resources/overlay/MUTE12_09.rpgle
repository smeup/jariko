     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 29/03/21  002768  BERNI Creato
     V* 29/03/21  V5R1    BMA   Check-out 002768 in SMEDEV
     V*=====================================================================
     DRESULT           S             35
     D                 DS
     D£K11SK                         35    DIM(10)
     D £K11S_NM                       5    OVERLAY(£K11SK:1)                    Nome
     D £K11S_VA                      30    OVERLAY(£K11SK:*NEXT)                Valore
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      *
     C                   CLEAR                   RESULT
     C                   EVAL      £K11S_NM(1)='PIPPO'                          COSTANTE
     C                   EVAL      £K11S_VA(1)='Descrizione'                    COSTANTE
     C                   EVAL      £K11S_NM(2)='ZAPPA'                          COSTANTE
     C                   EVAL      £K11S_VA(2)='Traduzione'                     COSTANTE
     C                   EVAL      £K11SK(3)='ASSO Soluzione'                   COSTANTE
      *
    MU* VAL1(RESULT) VAL2('PIPPODescrizione                  ') COMP(EQ)
     C                   EVAL      RESULT=£K11SK(1)
    MU* VAL1(RESULT) VAL2('ZAPPATraduzione                   ') COMP(EQ)
     C                   EVAL      RESULT=£K11SK(2)
    MU* VAL1(RESULT) VAL2('ASSO Soluzione                    ') COMP(EQ)
     C                   EVAL      RESULT=£K11SK(3)
    MU* VAL1(RESULT) VAL2('PIPPO                             ') COMP(EQ)
     C                   EVAL      RESULT=£K11S_NM(1)
    MU* VAL1(RESULT) VAL2('Descrizione                       ') COMP(EQ)
     C                   EVAL      RESULT=£K11S_VA(1)
    MU* VAL1(RESULT) VAL2('ZAPPA                             ') COMP(EQ)
     C                   EVAL      RESULT=£K11S_NM(2)
    MU* VAL1(RESULT) VAL2('Traduzione                        ') COMP(EQ)
     C                   EVAL      RESULT=£K11S_VA(2)
    MU* VAL1(RESULT) VAL2('ASSO                              ') COMP(EQ)
     C                   EVAL      RESULT=£K11S_NM(3)
    MU* VAL1(RESULT) VAL2('Soluzione                         ') COMP(EQ)
     C                   EVAL      RESULT=£K11S_VA(3)
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
