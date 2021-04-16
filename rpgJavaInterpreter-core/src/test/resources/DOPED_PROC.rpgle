      *---------------------------------------------------------------
      * Tested features:
      * CALLP OF A DOPED (NON-RPGLE) PROCEDURE 'JDP_SUM'
      *---------------------------------------------------------------
     D NUM1            S              3  0
     D NUM2            S              3  0
     D RES             S              3  0
      *
     D RES_CHAR        S             10
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
     C                   EVAL      NUM1=12
     C                   EVAL      NUM2=34
     C                   EVAL      RES=0
     C                   CALLP     JDP_SUM(NUM1:NUM2:RES)
      * Must be '46'
     C                   EVAL      RES_CHAR=%CHAR(RES)
     C     RES_CHAR      DSPLY
      *
     C                   SETON                                        LR