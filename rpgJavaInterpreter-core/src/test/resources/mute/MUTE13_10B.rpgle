      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * *INLR an *INRT boolean variables
      *
    MU* VAL1(*INRT) VAL2('1') COMP(EQ)
     C                   SETON                                        RT
      *
    MU* VAL1(*INLR) VAL2('1') COMP(EQ)
     C                   SETON                                        LR
      *
    MU* VAL1(*INRT) VAL2('0') COMP(EQ)
     C                   EVAL      *INRT = '0'
      *
    MU* VAL1(*INLR) VAL2('0') COMP(EQ)
     C                   EVAL      *INLR = '0'
      *
    MU* VAL1(*INRT) VAL2('1') COMP(EQ)
     C                   EVAL      *INRT = '1'
      *
    MU* VAL1(*INLR) VAL2('1') COMP(EQ)
     C                   EVAL      *INLR = '1'
      *
    MU* VAL1(*INRT) VAL2('0') COMP(EQ)
     C                   EVAL      *INRT = *OFF
      *
    MU* VAL1(*INLR) VAL2('0') COMP(EQ)
     C                   EVAL      *INLR = *OFF
      *
    MU* VAL1(*INRT) VAL2('1') COMP(EQ)
     C                   EVAL      *INRT = *ON
      *
    MU* VAL1(*INLR) VAL2('1') COMP(EQ)
     C                   EVAL      *INLR = *ON
      *
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
