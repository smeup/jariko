     D £DBG_Str        S             50          VARYING
     D A50_A1          S              7


     C     *LIKE         DEFINE    A50_A1        A50_A3
     C     *LIKE         DEFINE    A50_A3        A50_A4
     C                   EVAL      £DBG_Str= 'A50_A3('+A50_A3+')'
     C                                     +' A50_A4('+A50_A4+')'
     C     £DBG_Str      DSPLY