      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * *INLR an *INRT boolean variables
      *
     C                   SETON                                        35
     C                   SETON                                        36
    MU* VAL1(*IN35) VAL2('1') COMP(EQ)
     C   RT              SETOFF                                       35
    MU* VAL1(*IN36) VAL2('1') COMP(EQ)
     C   LR              SETOFF                                       36
      *
     C                   SETON                                        RT
     C                   SETON                                        LR
    MU* VAL1(*IN35) VAL2('0') COMP(EQ)
     C   RT              SETOFF                                       35
    MU* VAL1(*IN36) VAL2('0') COMP(EQ)
     C   LR              SETOFF                                       36
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
