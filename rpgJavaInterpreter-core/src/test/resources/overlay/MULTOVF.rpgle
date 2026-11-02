      *---------------------------------------------------------------
      * Fixed-form MULT does not raise an error when the result does not
      * fit the result field: the high-order digits are lost.
      * Expected values verified on IBM i (docs/ibmi-proof/RPGPROOF3).
      *---------------------------------------------------------------
     DA                S             12  0 INZ(338018318656)
     DB                S             12  0 INZ(338018318656)
     DC                S             22  8
     DX                S              9  0 INZ(123457)
     DY                S              9  0 INZ(10)
     DS                S              5  0
     DN1               S              9  0 INZ(-123457)
     DR                S             15  2
      *---------------------------------------------------------------
     C     A             MULT      B             C
    MU* VAL1(C) VAL2(47029157646336.00000000) COMP(EQ)
     C                   DSPLY                   C
     C     X             MULT      Y             S
    MU* VAL1(S) VAL2(34570) COMP(EQ)
     C                   DSPLY                   S
      * negative: sign is kept
     C     N1            MULT      Y             S
    MU* VAL1(S) VAL2(-34570) COMP(EQ)
     C                   DSPLY                   S
      * half adjust with overflow
     C     X             MULT(H)   Y             S
    MU* VAL1(S) VAL2(34570) COMP(EQ)
     C                   DSPLY                   S
      * no overflow: unchanged
     C     X             MULT      Y             R
    MU* VAL1(R) VAL2(1234570) COMP(EQ)
     C                   DSPLY                   R
      *
     C                   SETON                                        RT
