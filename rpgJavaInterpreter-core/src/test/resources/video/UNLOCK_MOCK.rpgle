     FUNLOCK_MV CF   E             WORKSTN SFILE(SUB:RRN)
     F                                     INFDS(DSSF01)

     D MSG             S             50          VARYING

     D  WFUND1         DS
     D  WSDATA                        8  0

     C                   UNLOCK      W$PERI
     C                   EVAL       MSG=''
     C     MSG           DSPLY
