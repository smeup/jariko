     D A               S             12  0 INZ(338018318656)
     D B               S             12  0 INZ(338018318656)
     D C               S             22  8
     D X               S              9  0 INZ(123457)
     D N               S              9  0 INZ(-123457)
     D Y               S              9  0 INZ(10)
     D S               S              5  0
     D R               S             15  2
     D £DBG_Str        S             52
      *
      * Fixed-form MULT does not raise an error when the result does not fit
      * the result field: the high-order digits are lost.
      * Expected values verified on IBM i (docs/ibmi-proof/RPGPROOF3).
      *
     C     A             MULT      B             C
     C                   EVAL      £DBG_Str=%CHAR(C)
      * Expected '47029157646336.00000000'
     C     £DBG_Str      DSPLY
      *
     C     X             MULT      Y             S
     C                   EVAL      £DBG_Str=%CHAR(S)
      * Expected '34570'
     C     £DBG_Str      DSPLY
      * the sign is kept
     C     N             MULT      Y             S
     C                   EVAL      £DBG_Str=%CHAR(S)
      * Expected '-34570'
     C     £DBG_Str      DSPLY
      * with half adjust
     C     X             MULT(H)   Y             S
     C                   EVAL      £DBG_Str=%CHAR(S)
      * Expected '34570'
     C     £DBG_Str      DSPLY
      * no overflow: unchanged
     C     X             MULT      Y             R
     C                   EVAL      £DBG_Str=%CHAR(R)
      * Expected '1234570.00'
     C     £DBG_Str      DSPLY
