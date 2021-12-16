      *---------------------------------------------------------------
      * Tested features:
      * EXECUTION OF A 'DOPED' (NON-RPGLE) PROCEDURE 'JDP_CALC'
      * - Try to pass parameter of non compatible type, must
      *   throws exception.
      *---------------------------------------------------------------
     DJDP_TYPES        PR
     D pN1                            2
     D pR1                            2  0
      *---------------------------------------------------------------
     D N1              S              2  0
     D R1              S              2  0
     D R2              S              2  0
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
      * 'CALLP'
     C                   EVAL      N1=12
     C                   EVAL      R1=0
     C                   CALLP     JDP_TYPES(N1:R1)
      *
      * 'EVAL'
     C                   EVAL      N1=56
     C                   EVAL      R1=0
     C                   EVAL      R2=JDP_TYPES(N1:R1)
      *
     C                   SETON                                        LR