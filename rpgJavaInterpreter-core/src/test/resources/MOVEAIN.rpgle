     D* £DBG_Ind        S             99
     D £DBG_Sch        S              1    DIM(99)
      *
     C                   MOVEL     *ON           *IN44
     C                   SETON                                        88
     C*                   MOVEA(P)  *IN           £DBG_Ind
     C                   MOVEA(P)  *IN           £DBG_Sch
      * Expected:
      * '000000000000000000000000000000000000000000000000000000000000000000000000000000000000000100000000000'
      *
     C*     £DBG_Ind      DSPLY
     C     £DBG_Sch      DSPLY
     C                   SETON                                        LR