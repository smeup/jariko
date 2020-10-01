     D FACTOR2         S              1  0
      *---------------------------------------------------------------
      * M A I N
      *---------------------------------------------------------------
     C                   EVAL      *IN99=*ON
     C                   Z-ADD     0             FACTOR2
      *
     C                   IF        *IN99=*OFF
     C                   Z-ADD     1             FACTOR2
     C                   ENDIF
      *
    MU* VAL1(FACTOR2) VAL2(1) COMP(EQ)
     C                   EVAL      FACTOR2+=1
      *
     C                   IF        *IN99=*OFF
     C                   Z-ADD     1             FACTOR2
     C                   ENDIF
      *
    MU* VAL1(FACTOR2) VAL2(2) COMP(EQ)
     C                   EVAL      FACTOR2+=1
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
