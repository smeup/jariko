     FPRT198    O    F  198        PRINTER OFLIND(*INOF) USROPN
     D £DBG_Str        S             50          VARYING
     C                   OPEN      PRT198
     C                   EXCEPT    E1WRIT
     C                   EVAL      £DBG_Str='EXCEPT works'
      * EXCEPT works
     C     £DBG_Str      DSPLY
     C                   SETON                                        LR
     OPRT198    E            E1WRIT      1
     O                                              'Exception string'