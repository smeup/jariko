      * Case 1
     C                   SETON                                        303132
     C                   SETON                                        40
     C                   SETON                                        41
     C                   SETON                                        42
     C                   SETON                                        LR  RT
    MU* VAL1(*INRT) VAL2('0') COMP(EQ)
     C   30
     CAN 31
     CAN 32
     CAN 40
     CAN 41
     CAN 42
     CAN RT
     CAN LR              EVAL      *INRT=*OFF
      *
      * Case 2
     C                   SETON                                        303132
     C                   SETOFF                                       40
     C                   SETOFF                                       41
     C                   SETOFF                                       42
     C                   SETOFF                                       LR  RT
    MU* VAL1(*INLR) VAL2('1') COMP(EQ)
     C   30
     CAN 31
     CAN 32
     CORN40
     CANN41
     CANN42
     CANNRT
     CANNLR              EVAL      *INLR='1'
      *
      * Case 3
     C                   SETON                                        30  32
     C                   SETON                                          31
    MU* VAL1(*INLR) VAL2('1') COMP(EQ)
     C   30
     CAN 31
     CAN 32
     CANNRT
     CANNLR              EVAL      *INLR='1'
      *
      * Case 4
     C                   SETON                                        3031
     C                   SETOFF                                       32
     C                   SETON                                          LR
    MU* VAL1(*INRT) VAL2('1') COMP(EQ)
     C   30
     CAN 31
     CANN32
     CANNRT
     CAN LR              EVAL      *INRT='1'
      *
      * Case 5
     C                   SETON                                        50OF
     C                   SETOFF                                         LR
     C                   EVAL      *INOV=*OFF
    MU* VAL1(*INOC) VAL2('1') COMP(EQ)
     C   50
     CAN OF
     CORNLR
     CANNOV              EVAL      *INOC=*ON
      *
     C                   SETON                                        LR