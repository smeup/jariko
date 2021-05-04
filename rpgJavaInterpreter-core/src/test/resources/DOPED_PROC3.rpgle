      *---------------------------------------------------------------
      * Tested features:
      * EXECUTION OF A 'DOPED' (NON-RPGLE) PROCEDURE 'JDP_CALC'
      * - Try to pass optional parameter
      *---------------------------------------------------------------
     DJDP_OPTIONAL     PR
     D pN1                            2  0
     D pN2                            2  0 OPTIONS(*NOPASS)
      *---------------------------------------------------------------
     D N1              S              2  0
     D N2              S              2  0
     D R1              S              2  0
     D RC              S              2
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
      * 'CALLP'
     C                   EVAL      N1=12
     C                   CALLP     JDP_OPTIONAL(N1)
      * Must be '12'
     C                   EVAL      RC=%CHAR(N1)
     C     RC            DSPLY
      *
     C                   EVAL      N1=12
     C                   EVAL      N2=34
     C                   CALLP     JDP_OPTIONAL(N1:N2)
      * Must be '46'
     C                   EVAL      RC=%CHAR(N1)
     C     RC            DSPLY
      *
      * 'EVAL'
     C                   EVAL      N1=56
     C                   EVAL      R1=JDP_OPTIONAL(N1)
      * Must be '56'
     C                   EVAL      RC=%CHAR(R1)
     C     RC            DSPLY
      *
     C                   EVAL      N1=56
     C                   EVAL      N2=43
     C                   EVAL      R1=JDP_OPTIONAL(N1:N2)
      * Must be '99'
     C                   EVAL      RC=%CHAR(R1)
     C     RC            DSPLY
      *
     C                   SETON                                        LR