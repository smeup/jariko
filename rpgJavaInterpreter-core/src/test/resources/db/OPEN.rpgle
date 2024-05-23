     FEMPLOY0F  IF   E           K DISK    USROPN
     D £DBG_Str        S             10          VARYING
     C                   EVAL      £DBG_Str='OK'
     C                   OPEN      EMPLOY0F
      * Execute clear of file record to test existence
     C                   CLEAR                   EMPLOYR
     C                   CLOSE     EMPLOY0F
     C     £DBG_Str      DSPLY