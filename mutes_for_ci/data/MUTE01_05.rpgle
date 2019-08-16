   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* 10/03/16  V4.R1   RM Creato
     V* 29/09/16  V5.R1   RM Gestione floating
     V* B£61020C  V5R1    BMA Aggiunta COP* *NOUI
     V* 07/02/17  V5R1  GIAGIU Implementazioni test
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test su campi di tipo BINARY
     V*
     V*=====================================================================
     DN                S              5S 0
      *
     DI1               S              3I 0
     DI2               S              5I 0
     DI3               S             10I 0
     DI4               S             20I 0
      *
     DF1               S              4F
     DF2               S              8F
      *
     DB1               S              1B 0
     DB2               S              2B 0
     DB3               S              3B 0
     DB4               S              4B 0
     DB5               S              5B 0
     DB6               S              6B 0
     DB7               S              7B 0
     DB8               S              8B 0
     DB9               S              9B 0
      *
     DB21              S              2B 1
     DB31              S              3B 1
     DB41              S              4B 1
     DB51              S              5B 1
     DB61              S              6B 1
     DB71              S              7B 1
     DB81              S              8B 1
     DB91              S              9B 1
      *
     DS1               S              2
     DP1               S               *
      *
     C                   EXSR      F_ADD
     C                   EXSR      F_CLEAR
     C                   EXSR      F_DIV
     C                   EXSR      F_EVAL
     C                   EXSR      F_MOVE
     C                   EXSR      F_MULT
     C                   EXSR      F_SUB
     C                   EXSR      F_Z
     C                   EXSR      F_OTHER
      *
    MU* Type="NOXMI"
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test ADD
      *---------------------------------------------------------------------
     C     F_ADD         BEGSR
      *
     C                   CLEAR                   I1
     C                   CLEAR                   I2
     C                   CLEAR                   I3
     C                   CLEAR                   I4
    MU* VAL1(I1) VAL2(1) COMP(EQ)
     C                   ADD       1             I1
    MU* VAL1(I2) VAL2(1) COMP(EQ)
     C                   ADD       1             I2
    MU* VAL1(I3) VAL2(1) COMP(EQ)
     C                   ADD       1             I3
    MU* VAL1(I4) VAL2(1) COMP(EQ)
     C                   ADD       1             I4
    MU* VAL1(I1) VAL2(11) COMP(EQ)
     C                   ADD       10            I1
    MU* VAL1(I2) VAL2(11) COMP(EQ)
     C                   ADD       10            I2
    MU* VAL1(I3) VAL2(11) COMP(EQ)
     C                   ADD       10            I3
    MU* VAL1(I4) VAL2(11) COMP(EQ)
     C                   ADD       10            I4
      *
     C                   CLEAR                   F1
     C                   CLEAR                   F2
    MU* VAL1(F1) VAL2(1) COMP(EQ)
     C                   ADD       1             F1
    MU* VAL1(F2) VAL2(1) COMP(EQ)
     C                   ADD       1             F2
      *
    MU* VAL1(B1) VAL2(1) COMP(EQ)
     C                   ADD       1             B1
    MU* VAL1(B2) VAL2(1) COMP(EQ)
     C                   ADD       1             B2
    MU* VAL1(B3) VAL2(1) COMP(EQ)
     C                   ADD       1             B3
    MU* VAL1(B4) VAL2(1) COMP(EQ)
     C                   ADD       1             B4
    MU* VAL1(B5) VAL2(1) COMP(EQ)
     C                   ADD       1             B5
    MU* VAL1(B6) VAL2(1) COMP(EQ)
     C                   ADD       1             B6
    MU* VAL1(B7) VAL2(1) COMP(EQ)
     C                   ADD       1             B7
    MU* VAL1(B8) VAL2(1) COMP(EQ)
     C                   ADD       1             B8
    MU* VAL1(B9) VAL2(1) COMP(EQ)
     C                   ADD       1             B9
      *
    MU* VAL1(B21) VAL2(1,4) COMP(EQ)
     C                   ADD       1,4           B21
    MU* VAL1(B31) VAL2(1,4) COMP(EQ)
     C                   ADD       1,4           B31
    MU* VAL1(B41) VAL2(1,4) COMP(EQ)
     C                   ADD       1,4           B41
    MU* VAL1(B51) VAL2(1,4) COMP(EQ)
     C                   ADD       1,4           B51
    MU* VAL1(B61) VAL2(1,4) COMP(EQ)
     C                   ADD       1,4           B61
    MU* VAL1(B71) VAL2(1,4) COMP(EQ)
     C                   ADD       1,4           B71
    MU* VAL1(B81) VAL2(1,4) COMP(EQ)
     C                   ADD       1,4           B81
    MU* VAL1(B91) VAL2(1,4) COMP(EQ)
     C                   ADD       1,4           B91
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test CLEAR
      *---------------------------------------------------------------------
     C     F_CLEAR       BEGSR
      *
    MU* VAL1(I1) VAL2(0) COMP(EQ)
     C                   CLEAR                   I1
    MU* VAL1(I2) VAL2(0) COMP(EQ)
     C                   CLEAR                   I2
    MU* VAL1(I3) VAL2(0) COMP(EQ)
     C                   CLEAR                   I3
    MU* VAL1(I4) VAL2(0) COMP(EQ)
     C                   CLEAR                   I4
      *
    MU* VAL1(F1) VAL2(0) COMP(EQ)
     C                   CLEAR                   F1
    MU* VAL1(F2) VAL2(0) COMP(EQ)
     C                   CLEAR                   F2
      *
    MU* VAL1(B1) VAL2(0) COMP(EQ)
     C                   CLEAR                   B1
    MU* VAL1(B2) VAL2(0) COMP(EQ)
     C                   CLEAR                   B2
    MU* VAL1(B3) VAL2(0) COMP(EQ)
     C                   CLEAR                   B3
    MU* VAL1(B4) VAL2(0) COMP(EQ)
     C                   CLEAR                   B4
    MU* VAL1(B5) VAL2(0) COMP(EQ)
     C                   CLEAR                   B5
    MU* VAL1(B6) VAL2(0) COMP(EQ)
     C                   CLEAR                   B6
    MU* VAL1(B7) VAL2(0) COMP(EQ)
     C                   CLEAR                   B7
    MU* VAL1(B8) VAL2(0) COMP(EQ)
     C                   CLEAR                   B8
    MU* VAL1(B9) VAL2(0) COMP(EQ)
     C                   CLEAR                   B9
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test DIV
      *---------------------------------------------------------------------
     C     F_DIV         BEGSR
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test EVAL
      *---------------------------------------------------------------------
     C     F_EVAL        BEGSR
      *
    MU* VAL1(I2) VAL2(-32768) COMP(EQ)
     C                   EVAL      I2=*LOVAL
    MU* VAL1(I2) VAL2(32767) COMP(EQ)
     C                   EVAL      I2=*HIVAL
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test MOVE
      *---------------------------------------------------------------------
     C     F_MOVE        BEGSR
      *
     C                   CLEAR                   S1
    MU* VAL1(B2) VAL2(0) COMP(EQ)
     C                   MOVEL     S1            B2
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test MULT
      *---------------------------------------------------------------------
     C     F_MULT        BEGSR
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test SUB
      *---------------------------------------------------------------------
     C     F_SUB         BEGSR
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test F_Z    ADD/SUB
      *---------------------------------------------------------------------
     C     F_Z           BEGSR
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine TEST
      *---------------------------------------------------------------------
     C     F_OTHER       BEGSR
      *
    MU* VAL1(N) VAL2(1) COMP(EQ)
     C                   EVAL      N=%size(I1)
    MU* VAL1(N) VAL2(2) COMP(EQ)
     C                   EVAL      N=%size(I2)
    MU* VAL1(N) VAL2(4) COMP(EQ)
     C                   EVAL      N=%size(I3)
    MU* VAL1(N) VAL2(8) COMP(EQ)
     C                   EVAL      N=%size(I4)
      *
    MU* VAL1(N) VAL2(3) COMP(EQ)
     C                   EVAL      N=%len(I1)
    MU* VAL1(N) VAL2(5) COMP(EQ)
     C                   EVAL      N=%len(I2)
    MU* VAL1(N) VAL2(10) COMP(EQ)
     C                   EVAL      N=%len(I3)
    MU* VAL1(N) VAL2(20) COMP(EQ)
     C                   EVAL      N=%len(I4)
      *
    MU* VAL1(N) VAL2(4) COMP(EQ)
     C                   EVAL      N=%size(F1)
    MU* VAL1(N) VAL2(8) COMP(EQ)
     C                   EVAL      N=%size(F2)
      *
    MU* VAL1(N) VAL2(4) COMP(EQ)
     C                   EVAL      N=%len(F1)
    MU* VAL1(N) VAL2(8) COMP(EQ)
     C                   EVAL      N=%len(F2)
      *
    MU* VAL1(N) VAL2(1) COMP(EQ)
     C                   EVAL      N=%len(B1)
    MU* VAL1(N) VAL2(2) COMP(EQ)
     C                   EVAL      N=%len(B2)
    MU* VAL1(N) VAL2(3) COMP(EQ)
     C                   EVAL      N=%len(B3)
    MU* VAL1(N) VAL2(4) COMP(EQ)
     C                   EVAL      N=%len(B4)
    MU* VAL1(N) VAL2(5) COMP(EQ)
     C                   EVAL      N=%len(B5)
    MU* VAL1(N) VAL2(6) COMP(EQ)
     C                   EVAL      N=%len(B6)
    MU* VAL1(N) VAL2(7) COMP(EQ)
     C                   EVAL      N=%len(B7)
    MU* VAL1(N) VAL2(8) COMP(EQ)
     C                   EVAL      N=%len(B8)
    MU* VAL1(N) VAL2(9) COMP(EQ)
     C                   EVAL      N=%len(B9)
      *
    MU* VAL1(N) VAL2(2) COMP(EQ)
     C                   EVAL      N=%size(B1)
    MU* VAL1(N) VAL2(2) COMP(EQ)
     C                   EVAL      N=%size(B2)
    MU* VAL1(N) VAL2(2) COMP(EQ)
     C                   EVAL      N=%size(B3)
    MU* VAL1(N) VAL2(2) COMP(EQ)
     C                   EVAL      N=%size(B4)
    MU* VAL1(N) VAL2(4) COMP(EQ)
     C                   EVAL      N=%size(B5)
    MU* VAL1(N) VAL2(4) COMP(EQ)
     C                   EVAL      N=%size(B6)
    MU* VAL1(N) VAL2(4) COMP(EQ)
     C                   EVAL      N=%size(B7)
    MU* VAL1(N) VAL2(4) COMP(EQ)
     C                   EVAL      N=%size(B8)
    MU* VAL1(N) VAL2(4) COMP(EQ)
     C                   EVAL      N=%size(B9)
      *
    MU* VAL1(N) VAL2(2) COMP(EQ)
     C                   EVAL      N=%len(B21)
    MU* VAL1(N) VAL2(3) COMP(EQ)
     C                   EVAL      N=%len(B31)
    MU* VAL1(N) VAL2(9) COMP(EQ)
     C                   EVAL      N=%len(B91)
    MU* VAL1(N) VAL2(2) COMP(EQ)
     C                   EVAL      N=%size(B21)
    MU* VAL1(N) VAL2(2) COMP(EQ)
     C                   EVAL      N=%size(B31)
    MU* VAL1(N) VAL2(4) COMP(EQ)
     C                   EVAL      N=%size(B91)
      *
     C                   ENDSR
