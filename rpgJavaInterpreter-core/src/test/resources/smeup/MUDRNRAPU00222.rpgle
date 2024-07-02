     D £DBG_Str        S             2

     D CSDS            DS
     D  $OAVT1                             LIKE(§OAVT1)

     C     *LIKE         DEFINE    £OAVT1        §OAVT1

     C     *ENTRY        PLIST
     C                   PARM                    £OAVT1            2

     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY