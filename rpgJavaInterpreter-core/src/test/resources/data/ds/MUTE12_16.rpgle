     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 19/03/21             Creazione
     V*=====================================================================
     V*
     V* OBIETTIVO
     V*  Programma finalizzato ai test su inizializzazione campi
     V*
     V*=====================================================================
     D £JaxWT          S                   LIKE(£JaxWE)
     D £JaxWT1         S                   LIKE(£JaxWE1)
     D £JaxWT2         S                   LIKE(£JaxWE2)
     D £JaxWT3         S                   LIKE(£JaxWE3)
     D £JaxWT4         S                   LIKE(£JaxWE4)
     D £JaxWT5         S                   LIKE(£JaxWE5)
     D £JaxWT6         S                   LIKE(£JaxWE6)
     D £JaxWT7         S                   LIKE(£JaxWE7)
     D £JaxWT8         S                   LIKE(£JaxWE8)
      *
     D £JaxDSCoda      DS
     D £JaxWE                         2  1 INZ
      *
     D £JaxDSCoda1     DS
     D £JaxWE1                        2  0 INZ
      *
     D £JaxDSCoda2     DS
     D £JaxWE2                        2
      *
     D £JaxDSCoda3     DS
     D £JaxWE3                        2    INZ
      *
     D £JaxDSCoda4     DS
     D £JaxWE4                        4    INZ('FOUR')
      *
     D £JaxDSCoda5     DS
     D £JaxWE5                        5  3
      *
     D £JaxDSCoda6     DS
     D £JaxWE6                        9  8 INZ(-1.12345678)
      *
     D £JaxDSCoda7     DS
     D £JaxWE7                        5  2 INZ
      *
     D £JaxDSCoda8     DS
     D £JaxWE8                        5    INZ
      *
     C                   EVAL      £JaxWT=8.1
     C                   EVAL      £JaxWT1=8
     C                   EVAL      £JaxWT2='81'
     C                   EVAL      £JaxWT5=12.345
     C                   EVAL      £JaxWT7=123.456789
     C                   EVAL      £JaxWT8='1234567890'
     C                   EVAL      £JaxWT6=-0.12345678
      *
    MU* VAL1(£JaxWE) VAL2(0.0) COMP(EQ)
     C                   SETON                                        50
      *
    MU* VAL1(£JaxWE1) VAL2(0) COMP(EQ)
     C                   SETON                                        50
      *
    MU* VAL1(£JaxWE2) VAL2('  ') COMP(EQ)
     C                   SETON                                        50
      *
    MU* VAL1(£JaxWE3) VAL2('  ') COMP(EQ)
     C                   SETON                                        50
      *
    MU* VAL1(£JaxWE4) VAL2('FOUR') COMP(EQ)
     C                   SETON                                        50
      *
    MU* VAL1(£JaxWT) VAL2(8.1) COMP(EQ)
     C                   SETON                                        50
      *
    MU* VAL1(£JaxWT1) VAL2(8) COMP(EQ)
     C                   SETON                                        50
      *
    MU* VAL1(£JaxWT2) VAL2('81') COMP(EQ)
     C                   SETON                                        50
      *
    MU* VAL1(£JaxWT3) VAL2('  ') COMP(EQ)
     C                   SETON                                        50
      *
    MU* VAL1(£JaxWT4) VAL2('    ') COMP(EQ)
     C                   SETON                                        50
      *
    MU* VAL1(£JaxWT5) VAL2(12.345) COMP(EQ)
     C                   SETON                                        50
      *
    MU* VAL1(£JaxWT6) VAL2(-0.12345678) COMP(EQ)
     C                   SETON                                        50
      *
    MU* VAL1(£JaxWT7) VAL2(123.45) COMP(EQ)
     C                   SETON                                        50
      *
    MU* VAL1(£JaxWT8) VAL2('12345') COMP(EQ)
     C                   SETON                                        50
      *
     C                   SETON                                        LR
