     D BOOL            S               N                                        Boolean
      *
     DBOOLDS           DS                                                       Boolean DS
     D BOOLDS1                         N
     D BOOLDS2                         N
     D BOOLDS3                         N
      *
    MU* VAL1(BOOL) VAL2('1') COMP(EQ)
     C                   EVAL      BOOL = *ON
      *
    MU* VAL1(BOOLDS1) VAL2('1') COMP(EQ)
     C                   EVAL      BOOLDS1 = *ON
    MU* VAL1(BOOLDS2) VAL2('1') COMP(EQ)
     C                   EVAL      BOOLDS2 = *ON
    MU* VAL1(BOOLDS3) VAL2('1') COMP(EQ)
     C                   EVAL      BOOLDS3 = *ON
      *
     C                   SETON                                        LR
