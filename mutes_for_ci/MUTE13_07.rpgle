   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 28/10/19  001221 BMA Creato
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test sulla MULT
     V*
     V*=====================================================================
     DP2               S              5P 0
     DP3               S             15P 5
     DP6               S             12P 2
      * Variabili
     C                   EXSR      F_MULT
      *
    MU* Type="NOXMI"
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test MULT
      *---------------------------------------------------------------------
     C     F_MULT        BEGSR
      *
     C                   CLEAR                   P2
     C                   CLEAR                   P3
     C                   CLEAR                   P6
    MU* VAL1(P2) VAL2(10) COMP(EQ)
     C     2             MULT      5             P2
    MU* VAL1(P2) VAL2(50) COMP(EQ)
     C                   MULT      5             P2
    MU* VAL1(P2) VAL2(-10) COMP(EQ)
     C     2             MULT      -5            P2
    MU* VAL1(P2) VAL2(50) COMP(EQ)
     C                   MULT      -5            P2
    MU* VAL1(P2) VAL2(50) COMP(EQ)
     C                   MULT(H)   1             P2
    MU* VAL1(P2) VAL2(10) COMP(EQ)
     C     2             MULT      5,12345       P2
    MU* VAL1(P2) VAL2(-10) COMP(EQ)
     C     2             MULT      -5,12345      P2
      *
    MU* VAL1(P3) VAL2(10,2469) COMP(EQ)
     C     2             MULT      5,12345       P3
    MU* VAL1(P3) VAL2(-10,2469) COMP(EQ)
     C     2             MULT      -5,12345      P3
    MU* VAL1(P3) VAL2(10,2469) COMP(EQ)
     C     2             MULT(H)   5,12345       P3
    MU* VAL1(P3) VAL2(-10,2469) COMP(EQ)
     C     2             MULT(H)   -5,12345      P3
      *
    MU* VAL1(P6) VAL2(11,97) COMP(EQ)
     C     2             MULT      5,98765       P6
    MU* VAL1(P6) VAL2(-11,97) COMP(EQ)
     C     2             MULT      -5,98765      P6
    MU* VAL1(P6) VAL2(11,98) COMP(EQ)
     C     2             MULT(H)   5,98765       P6
    MU* VAL1(P6) VAL2(-11,98) COMP(EQ)
     C     2             MULT(H)   -5,98765      P6
      *
     C                   ENDSR
