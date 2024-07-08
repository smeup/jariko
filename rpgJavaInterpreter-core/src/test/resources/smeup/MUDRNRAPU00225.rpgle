     D £DBG_Str        S             2

     D B£SLOT        E DS                  EXTNAME(B£SLOT0F) INZ
     D CSLOTS          S                   LIKE(B£SLOT) DIM(200)
     D* skiera ordinata delle chiavi
     D CSLOTADS        DS
     D CSLOTA                              LIKE(CSDS)
     D                                     DIM(%ELEM(CSLOTS)) ASCEND
     D* CSLOTKA                             OVERLAY(CSLOTA:01) LIKE(CKEY)        Chiave ordinamento
     D CSLOTNA                             OVERLAY(CSLOTA:*NEXT) LIKE(CSNR)     Puntatore a CSLOTS

     D CSDS            DS
     D  $OAVT1                             LIKE(§OAVT1)
     D  $OAVP1                             LIKE(§OAVP1)
     D  $OAVT2                             LIKE(§OAVT2)
     D  $OAVP2                             LIKE(§OAVP2)
     D  $OAVAT                             LIKE(§OAVAT)
     D  CSNR                          5S 0

     C     *ENTRY        PLIST
     C                   PARM                    £OAVT1            2
     C                   PARM                    £OAVP1           10
     C                   PARM                    £OAVT2            2
     C                   PARM                    £OAVP2           10
     C                   PARM                    £OAVAT           15

     C     *LIKE         DEFINE    £OAVT1        §OAVT1
     C     *LIKE         DEFINE    £OAVP1        §OAVP1
     C     *LIKE         DEFINE    £OAVT2        §OAVT2
     C     *LIKE         DEFINE    £OAVP2        §OAVP2
     C     *LIKE         DEFINE    £OAVAT        §OAVAT

     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY