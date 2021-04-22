      *---------------------------------------------------------------
      * Tested features:
      * EXECUTION OF A 'DOPED' (NON-RPGLE) PROCEDURE 'JDP_CALC'
      * - If procedure is called by 'CALLP' statement, result will be
      *   set to 'R1' variable (accordling to 'ParameterPassedBy' policy)
      * - If procedure is called by 'EVAL' statement, result will be
      *   set to 'R2' variable.
      *---------------------------------------------------------------
     DJDP_CALC         PR
     D pN1                            2  0 VALUE
     D pN2                            2  0 VALUE
     D pOP                            1    VALUE
     D pR1                            2  0
      *---------------------------------------------------------------
     D N1              S              2  0
     D N2              S              2  0
     D OP              S              1
     D R1              S              2  0
     D R2              S              2  0
     D RC              S             10
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
      * '+' operator by 'CALLP'
     C                   EVAL      N1=12
     C                   EVAL      N2=34
     C                   EVAL      OP='+'
     C                   EVAL      R1=0
     C                   CALLP     JDP_CALC(N1:N2:OP:R1)
      * Must be '46'
     C                   EVAL      RC=%CHAR(R1)
     C     RC            DSPLY
      * Must be '12'
     C                   EVAL      RC=%CHAR(N1)
     C     RC            DSPLY
      *
      * '-' operator by 'EVAL'
     C                   EVAL      N1=56
     C                   EVAL      N2=78
     C                   EVAL      OP='-'
     C                   EVAL      R1=0
     C                   EVAL      R2=JDP_CALC(N1:N2:OP:R1)
      * Must be '-22'
     C                   EVAL      RC=%CHAR(R2)
     C     RC            DSPLY
      * Must be '56'
     C                   EVAL      RC=%CHAR(N1)
     C     RC            DSPLY
      *
      * '*' operator by 'CALLP'
     C                   EVAL      N1=7
     C                   EVAL      N2=8
     C                   EVAL      OP='*'
     C                   EVAL      R1=0
     C                   CALLP     JDP_CALC(N1:N2:OP:R1)
      * Must be '56'
     C                   EVAL      RC=%CHAR(R1)
     C     RC            DSPLY
      * Must be '7'
     C                   EVAL      RC=%CHAR(N1)
     C     RC            DSPLY
      *
      * '/' operator by 'EVAL'
     C                   EVAL      N1=30
     C                   EVAL      N2=6
     C                   EVAL      OP='/'
     C                   EVAL      R1=0
     C                   EVAL      R2=JDP_CALC(N1:N2:OP:R1)
      * Must be '5'
     C                   EVAL      RC=%CHAR(R2)
     C     RC            DSPLY
      * Must be '30'
     C                   EVAL      RC=%CHAR(N1)
     C     RC            DSPLY
      *
     C                   SETON                                        LR