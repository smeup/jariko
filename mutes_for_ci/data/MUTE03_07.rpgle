   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* 16/01/17  V5.R1   GIAGIU Creato
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test sulle definizione di Aray in DS con Overlay
     V*
     V*=====================================================================
     D DS01            DS
     D AR01                          15    DIM(10)
     D  SB01                          5    OVERLAY(AR01:1)
     D  SSB01                         1    OVERLAY(SB01:1)
     D  SSB02                         1    OVERLAY(SB01:*NEXT)
     D  SSB03                         1    OVERLAY(SB01:*NEXT)
     D  SB02                         10    OVERLAY(AR01:*NEXT)
      *
     D DS02            DS
     D AR02                          10    DIM(10)
     D  SB11                          1    OVERLAY(AR02:1)
     D  SB12                          1    OVERLAY(AR02:*NEXT)
     D  SB13                          1    OVERLAY(AR02:*NEXT)
      *
     C                   CLEAR                   AR01
      * Overlay di overlay
    MU* VAL1(SB01(01)) VAL2('AAAAA') COMP(EQ)
    MU* VAL1(SSB01(01)) VAL2('A') COMP(EQ)
    MU* VAL1(SSB02(01)) VAL2('A') COMP(EQ)
    MU* VAL1(SSB03(01)) VAL2('A') COMP(EQ)
    MU* VAL1(SB01(02)) VAL2('     ') COMP(EQ)
    MU* VAL1(SSB01(02)) VAL2(' ') COMP(EQ)
    MU* VAL1(SSB02(02)) VAL2(' ') COMP(EQ)
    MU* VAL1(SSB03(02)) VAL2(' ') COMP(EQ)
     C                   EVAL      AR01(01)=*ALL'A'
    MU* VAL1(AR01(02)) VAL2('BBBBBBBBBBBBBBB') COMP(EQ)
     C                   EVAL      AR01(02)=*ALL'B'
    MU* VAL1(AR01(03)) VAL2('CCCCCCCCCCCCCCC') COMP(EQ)
     C                   EVAL      AR01(03)=*ALL'C'
      *
    MU* VAL1(SB01(01)) VAL2('123  ') COMP(EQ)
    MU* VAL1(SSB01(01)) VAL2('1') COMP(EQ)
    MU* VAL1(SSB02(01)) VAL2('2') COMP(EQ)
    MU* VAL1(SSB03(01)) VAL2('3') COMP(EQ)
    MU* VAL1(SSB01(02)) VAL2('B') COMP(EQ)
    MU* VAL1(SSB02(02)) VAL2('B') COMP(EQ)
    MU* VAL1(SSB03(02)) VAL2('B') COMP(EQ)
    MU* VAL1(SSB01(03)) VAL2('C') COMP(EQ)
    MU* VAL1(SSB02(03)) VAL2('C') COMP(EQ)
    MU* VAL1(SSB03(03)) VAL2('C') COMP(EQ)
    MU* VAL1(SSB01(04)) VAL2(' ') COMP(EQ)
    MU* VAL1(SSB02(04)) VAL2(' ') COMP(EQ)
    MU* VAL1(SSB03(04)) VAL2(' ') COMP(EQ)
    MU* VAL1(AR01(01)) VAL2('123  AAAAAAAAAA') COMP(EQ)
     C                   EVAL      SB01(01)='123  '
      *
     C                   EVAL      AR02(01)='1234567890'
     C                   EVAL      AR02(02)='ABCDEFGHIL'
     C                   EVAL      AR02(03)='MNOPQRSTUV'
      *
    MU* Type="NOXMI"
     C                   SETON                                        LR
