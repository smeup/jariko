     FB£DIR40V  CF   E             WORKSTN SFILE(B£DIR40VSUB:RRN)
     F                                     INFDS(DSSF01)

     D MSG             S             50          VARYING

     D  WFUND1         DS
     D  WSDATA                        8  0

     C                   UNLOCK      W$PERI
     C                   EVAL       MSG=''
     C     MSG           DSPLY
