     D £DBG_Str        S             2
     D B£SLOT        E DS                  EXTNAME(B£SLOT0F)

     D CSDS            DS
     D  CSNR                          5S 0

     D CSLOTS          S                   LIKE(B£SLOT) DIM(200)
     D CSLOTADS        DS

     D CSLOTA                              LIKE(CSDS)
     D                                     DIM(%ELEM(CSLOTS)) ASCEND

     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY