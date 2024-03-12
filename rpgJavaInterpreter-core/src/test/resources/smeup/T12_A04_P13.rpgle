     D A04_A01_CNT     S              1    INZ('')
     D £DBG_Str        S            150          VARYING

     C                   EVAL      A04_A01_CNT = 'A'
     C     A04_A01_CNT   DOWNE     *BLANKS
     C                   EVAL      A04_A01_CNT = *BLANKS
     C                   ENDDO
     C                   EVAL      £DBG_Str='CNT('+%TRIM(A04_A01_CNT)+')'
     C     £DBG_Str      DSPLY