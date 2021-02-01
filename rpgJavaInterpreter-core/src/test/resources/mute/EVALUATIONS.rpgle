     D R               S             10  3                                       Result
     D V01             S             10  3                                       Value01
     D V02             S             10  3                                       Value02
      *
     C                   EVAL      V01 = 4
     C                   EVAL      V02 = 2
    MU* VAL1(R) VAL2(2) COMP(EQ)
     C                   EVAL      R = V01 / -V02
      *
     C                   SETON                                        LR