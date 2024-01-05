     D A80_N1          S              6  0
     D A80_N2          S             12  0
     D A80_N3          S             14  0
     D £DBG_Str        S             52
      *
     C                   TIME                    A80_N1
     C                   EVAL      £DBG_Str=%CHAR(A80_N1)
      * Expected number in HmmSS format
     C     £DBG_Str      DSPLY
      *
     C                   TIME                    A80_N2
     C                   EVAL      £DBG_Str=%CHAR(A80_N2)
      * Expected numer in HmmSSDDMMYY
     C     £DBG_Str      DSPLY
      *
     C                   TIME                    A80_N3
     C                   EVAL      £DBG_Str=%CHAR(A80_N3)
      * Expected numer in HmmSSDDMMYYYY
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR