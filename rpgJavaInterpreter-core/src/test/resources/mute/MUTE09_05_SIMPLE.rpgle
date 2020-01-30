     D ARRA2           S             10    DIM(10)                              Array 2
     D ZZSTR           S             20
      *----------------------------------------------------------------------------------
     C                   EVAL      ARRA2=*ALL'X'
    MU* VAL1(ZZSTR) VAL2('YYYYYYYYYYYYYYYYYYYY') COMP(EQ)
     C                   EVAL      ZZSTR=*ALL'Y'
    MU* VAL1(ZZSTR) VAL2('XXXXXXXXXXYYYYYYYYYY') COMP(EQ)
     C                   MOVEL     ARRA2(2)      ZZSTR
      *----------------------------------------------------------------------------------
     C                   SETON                                        LR
