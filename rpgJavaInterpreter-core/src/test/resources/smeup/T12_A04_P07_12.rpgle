     D A04_N50_LIM     S              6  0 INZ(100000)
     D A04_N50_CNT     S                   LIKE(A04_N50_LIM)
     D £DBG_Str        S             100          VARYING

      *---------------------------------------------------------------
    RD* Errori programma MULANGT12 sezione A04
      *--------------------------------------------------------------*
    OA* A£.CDOP(DOWEQ;ENDDO)
     D* DOWEQ
     C                   EVAL      A04_N50_CNT = A04_N50_LIM
     C     A04_N50_CNT   DOWEQ     A04_N50_LIM
     C                   EVAL      A04_N50_CNT = A04_N50_CNT + 1
     C                   ENDDO
     C                   EVAL      £DBG_Str='CNT('+%CHAR(A04_N50_CNT)+')'
     C      £DBG_Str     DSPLY
    OA* A£.CDOP(DOWNE;ENDDO)
     D* DOWNE
     C                   EVAL      A04_N50_CNT = 0
     C     A04_N50_CNT   DOWNE     A04_N50_LIM
     C                   EVAL      A04_N50_CNT = A04_N50_CNT + 1
     C                   ENDDO
     C                   EVAL      £DBG_Str='CNT('+%CHAR(A04_N50_CNT)+')'
     C      £DBG_Str     DSPLY
    OA* A£.CDOP(DOWGE;ENDDO)
     D* DOWGE
     C                   EVAL      A04_N50_CNT = 0
     C     A04_N50_LIM   DOWGE     A04_N50_CNT
     C                   EVAL      A04_N50_CNT = A04_N50_CNT + 1
     C                   ENDDO
     C                   EVAL      £DBG_Str='CNT('+%CHAR(A04_N50_CNT)+')'
     C      £DBG_Str     DSPLY
    OA* A£.CDOP(DOWGT;ENDDO)
     D* DOWGT
     C                   EVAL      A04_N50_CNT = 0
     C     A04_N50_LIM   DOWGT     A04_N50_CNT
     C                   EVAL      A04_N50_CNT = A04_N50_CNT + 1
     C                   ENDDO
     C                   EVAL      £DBG_Str='CNT('+%CHAR(A04_N50_CNT)+')'
     C      £DBG_Str     DSPLY
    OA* A£.CDOP(DOWLE;ENDDO)
     D* DOWLE
     C                   EVAL      A04_N50_CNT = 0
     C     A04_N50_CNT   DOWLE     A04_N50_LIM
     C                   EVAL      A04_N50_CNT = A04_N50_CNT + 1
     C                   ENDDO
     C                   EVAL      £DBG_Str='CNT('+%CHAR(A04_N50_CNT)+')'
     C      £DBG_Str     DSPLY
    OA* A£.CDOP(DOWLT;ENDDO)
     D* DOWLT
     C                   EVAL      A04_N50_CNT = 0
     C     A04_N50_CNT   DOWLT     A04_N50_LIM
     C                   EVAL      A04_N50_CNT = A04_N50_CNT + 1
     C                   ENDDO
     C                   EVAL      £DBG_Str='CNT('+%CHAR(A04_N50_CNT)+')'
     C      £DBG_Str     DSPLY