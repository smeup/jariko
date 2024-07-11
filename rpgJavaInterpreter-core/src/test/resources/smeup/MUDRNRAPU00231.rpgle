     D £DBG_Str        S             2

     D CSDS            DS
     D  $OAVT1                             LIKE(§OAVT1)
     D  $OAVP1                             LIKE(§OAVP1)
     D  $OAVT2                             LIKE(§OAVT2)
     D  $OAVP2                             LIKE(§OAVP2)
     D  $OAVAT                             LIKE(§OAVAT)
     D  CSNR                          5S 0

     C     *LIKE         DEFINE    £OAVT1        §OAVT1
     C     *LIKE         DEFINE    £OAVP1        §OAVP1
     C     *LIKE         DEFINE    £OAVT2        §OAVT2
     C     *LIKE         DEFINE    £OAVP2        §OAVP2
     C     *LIKE         DEFINE    £OAVAT        §OAVAT

     C     £INIZI        BEGSR
     C     *ENTRY        PLIST
     C                   PARM                    £OAVT1            2
     C                   PARM                    £OAVP1           10
     C                   PARM                    £OAVT2            2
     C                   PARM                    £OAVP2           10
     C                   PARM                    £OAVAT           15
     C                   ENDSR

     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY