   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 05/03/21  002677  BERNI  Aggiunto esempio di Like di un campo di una DS
     V* 05/03/21  V5R1    BMA    Check-out 002677 in SMEDEV
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test sulla definizione di campi con LIKE Define
     V*
     V*=====================================================================
     D DS01            DS
     D N01                           10S 0
     D N02                           10P 0
     D N03                           10  0
      *
     D N04             S             10S 0
     D N05             S             10P 0
     D N06             S             10  0
      *
     D NN08            S                   LIKE(N01)
     D NN09            S                   LIKE(N02)
     D NN10            S                   LIKE(N03)
     D NN11            S                   LIKE(N04)
     D NN12            S                   LIKE(N05)
     D NN13            S                   LIKE(N06)
      *
     D NN16            S                   LIKE(N07)
     D NN17            S                   LIKE(DS0000)
     D NN18            S                   LIKE(DS0001)
      *
     D DS00DS          DS                  INZ
     D  DS0000                01     10
     D  DS0001                11     18  0
      *
     D XXSTR                         10
      *
     C     *LIKE         DEFINE    N01           NN01
     C     *LIKE         DEFINE    N02           NN02
     C     *LIKE         DEFINE    N03           NN03
     C     *LIKE         DEFINE    N04           NN04
     C     *LIKE         DEFINE    N05           NN05
     C     *LIKE         DEFINE    N06           NN06
     C     *LIKE         DEFINE    N07           NN07
      *
     C                   CLEAR                   N07              10 0
    MU* VAL1(XXSTR) VAL2(        10) COMP(EQ)
     C                   EVAL      NN01=10
     C                   EVAL      XXSTR=%EDITC(NN01:'Z')
    MU* VAL1(XXSTR) VAL2(        10) COMP(EQ)
     C                   EVAL      NN02=10
     C                   EVAL      XXSTR=%EDITC(NN02:'Z')
    MU* VAL1(XXSTR) VAL2(        10) COMP(EQ)
     C                   EVAL      NN03=10
     C                   EVAL      XXSTR=%EDITC(NN03:'Z')
    MU* VAL1(XXSTR) VAL2(        10) COMP(EQ)
     C                   EVAL      NN04=10
     C                   EVAL      XXSTR=%EDITC(NN04:'Z')
    MU* VAL1(XXSTR) VAL2(        10) COMP(EQ)
     C                   EVAL      NN05=10
     C                   EVAL      XXSTR=%EDITC(NN05:'Z')
    MU* VAL1(XXSTR) VAL2(        10) COMP(EQ)
     C                   EVAL      NN06=10
     C                   EVAL      XXSTR=%EDITC(NN06:'Z')
    MU* VAL1(XXSTR) VAL2(        10) COMP(EQ)
     C                   EVAL      NN07=10
     C                   EVAL      XXSTR=%EDITC(NN07:'Z')

    MU* VAL1(XXSTR) VAL2(        10) COMP(EQ)
     C                   EVAL      NN08=10
     C                   EVAL      XXSTR=%EDITC(NN08:'Z')
    MU* VAL1(XXSTR) VAL2(        10) COMP(EQ)
     C                   EVAL      NN09=10
     C                   EVAL      XXSTR=%EDITC(NN09:'Z')
    MU* VAL1(XXSTR) VAL2(        10) COMP(EQ)
     C                   EVAL      NN10=10
     C                   EVAL      XXSTR=%EDITC(NN10:'Z')
    MU* VAL1(XXSTR) VAL2(        10) COMP(EQ)
     C                   EVAL      NN11=10
     C                   EVAL      XXSTR=%EDITC(NN11:'Z')
    MU* VAL1(XXSTR) VAL2(        10) COMP(EQ)
     C                   EVAL      NN12=10
     C                   EVAL      XXSTR=%EDITC(NN12:'Z')
    MU* VAL1(XXSTR) VAL2(        10) COMP(EQ)
     C                   EVAL      NN13=10
     C                   EVAL      XXSTR=%EDITC(NN13:'Z')
    MU* VAL1(XXSTR) VAL2(        10) COMP(EQ)
     C                   EVAL      NN16=10
     C                   EVAL      XXSTR=%EDITC(NN16:'Z')
    MU* VAL1(XXSTR) VAL2(10        ) COMP(EQ)
     C                   EVAL      NN17='10'
     C                   EVAL      XXSTR=NN17
    MU* VAL1(XXSTR) VAL2(      10  ) COMP(EQ)
     C                   EVAL      NN18=10
     C                   EVAL      XXSTR=%EDITC(NN18:'Z')
      *
     C                   SETON                                        LR
