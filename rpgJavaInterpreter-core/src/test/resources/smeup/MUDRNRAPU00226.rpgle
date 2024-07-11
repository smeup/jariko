     D £DBG_Str        S             2
     D B£SLOT        E DS                  EXTNAME(B£SLOT0F) INZ

     D CSDS            DS
     D  CSNR                          5S 0

     D CSLOTS          S                   LIKE(B£SLOT) DIM(200) INZ(*HIVAL)
     D CSLOTADS        DS

     D CSLOTA                              LIKE(CSDS) INZ(*HIVAL)
     D                                     DIM(%ELEM(CSLOTS)) ASCEND

     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY