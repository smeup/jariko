   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 20/08/19  001071  BMA Creazione
     V* 22/08/19  001071  BMA Aggiunti sottocampi
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test slla definizione delle DS
     V*
     V*=====================================================================
     D ARDS            DS
     D AR01                                DIM(100) ASCEND
     D  FI01                         15    OVERLAY(AR01:1)
     D  FI02                         10    OVERLAY(AR01:*NEXT)
     D  FI03                         35    OVERLAY(AR01:*NEXT)
     D  FI04                          1    OVERLAY(AR01:*NEXT)
     D  FI05                          1    OVERLAY(AR01:*NEXT)
     D  FI06                          1    OVERLAY(AR01:*NEXT)
     D  FI07                         15  3 OVERLAY(AR01:*NEXT)
     D  FI07A                        15    OVERLAY(FI07:1)
     D  FI08                          1    OVERLAY(AR01:*NEXT)
     D  FI09                          1    OVERLAY(AR01:*NEXT)
     D  FI10                         15P 3 OVERLAY(AR01:*NEXT)
      * Binary da 2 byte
     D  FI11                          2B 0 OVERLAY(AR01:*NEXT)
      * Binary da 4 byte
     D  FI12                          4B 0 OVERLAY(AR01:*NEXT)
      * Integer da 1 byte
     D  FI13                          3I 0 OVERLAY(AR01:*NEXT)
      * Integer da 2 byte
     D  FI14                          5I 0 OVERLAY(AR01:*NEXT)
      * Integer da 4 byte
     D  FI15                         10I 0 OVERLAY(AR01:*NEXT)
      * Integer da 8 byte
     D  FI16                         20I 0 OVERLAY(AR01:*NEXT)
      * Integer da 1 byte unsigned
     D  FI17                          3U 0 OVERLAY(AR01:*NEXT)
      * Integer da 2 byte unsigned
     D  FI18                          5U 0 OVERLAY(AR01:*NEXT)
      * Integer da 4 byte unsigned
     D  FI19                         10U 0 OVERLAY(AR01:*NEXT)
      * Integer da 8 byte unsigned
     D  FI20                         20U 0 OVERLAY(AR01:*NEXT)
      *
      * Riferimento £C£E
     D                 DS
     D LOG1                          14    INZ('0F0L1L2L3L4L5L')                COSTANTE
     D LOG                            2    OVERLAY(LOG1:1) DIM(7)
      * Riferimento programma P5P5H0
     D                 DS
     D KEV                                 DIM(200)
     D KEVRES                        12    OVERLAY(KEV:01)
     D KEVTEV                         3    OVERLAY(KEV:*NEXT)
     D KEVSCT                         2    OVERLAY(KEV:*NEXT)
     D KEVCAT                         3    OVERLAY(KEV:*NEXT)
      * KEVORD corrisponde a KEVRES+KEVTEV
     D KEVORD                        15    OVERLAY(KEV:1)
      *
     D N1              S              7  0
      *--------------------------------------------
    MU* VAL1(N1) VAL2(122) COMP(EQ)
     C                   EVAL      N1=%LEN(AR01(01))
    MU* VAL1(N1) VAL2(12200) COMP(EQ)
     C                   EVAL      N1=%LEN(ARDS)
      * Utilizzo *HIVAL e *LOVAL insieme alle annotazioni per testare il range di valori validi
    MU* VAL1(FI07(001)) VAL2(999999999999,999) COMP(EQ)
    MU* VAL1(FI07A(001)) VAL2('999999999999999') COMP(EQ)
    MU* VAL1(FI07(100)) VAL2(999999999999,999) COMP(EQ)
    MU* VAL1(FI07A(100)) VAL2('999999999999999') COMP(EQ)
     C                   EVAL      FI07=*HIVAL
      *
    MU* VAL1(FI07(001)) VAL2(-999999999999,999) COMP(EQ)
      * In rappresentazione stringa di uno zoned negativo l'ultima cifra diventa una lettera :
      *    -1      J
      *    -2      K
      *    -3      L
      *    -4      M
      *    -5      N
      *    -6      O
      *    -7      P
      *    -8      Q
      *    -9      R
     C                   SETON                                        LR
