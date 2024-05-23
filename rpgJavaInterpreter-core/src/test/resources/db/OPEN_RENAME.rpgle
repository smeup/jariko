     FEMPLOY0F  IF   E           K DISK    USROPN
     F                                     RENAME(EMPLOYR:EMPLOYU)
     D £DBG_Str        S             10          VARYING
     C                   EVAL      £DBG_Str='OK'
     C                   OPEN      EMPLOY0F
      * Execute clear of renamed file record
     C                   CLEAR                   EMPLOYU
     C                   CLOSE     EMPLOY0F
     C     £DBG_Str      DSPLY