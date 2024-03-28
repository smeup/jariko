     D £DBG_Ind        S             99
      *
     C                   MOVEL     *ON           *IN44
     C                   MOVE      *ON           *IN03
     C                   SETON                                        88
     C                   SETON                                        0102
     C                   MOVEA(P)  *IN           £DBG_Ind
      * Expected:
      * '111000000000000000000000000000000000000000010000000000000000000000000000000000000000000100000000000'
     C     £DBG_Ind      DSPLY
      *
     C                   SETOFF                                       0102
     C*                   MOVEL     '1'           *IN03
     C                   MOVEA(P)  *IN           £DBG_Ind
      * Expected:
      * '000000000000000000000000000000000000000000010000000000000000000000000000000000000000000100000000000'
     C     £DBG_Ind      DSPLY
      *
     C                   SETON                                        LR