     FJALOGT0F  UF A E             DISK    USROPN
     DJALOGT         E DS                  EXTNAME(JALOGT0F)
     DSJALOG           S                   LIKE(JALOGT)
     D £DBG_Str        S             2

     C                   EVAL      SJALOG=JALOGT
     C                   EVAL      JALOGT=SJALOG
     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY