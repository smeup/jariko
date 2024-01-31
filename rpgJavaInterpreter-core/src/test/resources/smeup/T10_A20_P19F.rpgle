     D £DBG_Str        S             50          VARYING
     D A20_AR1         S              2  0 DIM(2)
     ***************************************************************************
     C                   Z-ADD     10            A20_AR1(1
     C                   Z-ADD     20            A20_AR12)
     C                   EVAL      £DBG_Str=%CHAR(A20_AR1(1))
     C                                     +%CHAR(A20_AR1(2))
      * EXPECTED: 1020
     C     £DBG_Str      DSPLY
     C                   SETON                                        LR