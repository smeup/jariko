     D A50_A1          S              7
     D A50_N1          S              5  2
     D £DBG_Str        S             50
     D £DBG_Pas        S              3
      *
     C                   EXSR      SEZ_A50
     C                   SETON                                          LR
      *
     C     SEZ_A50       BEGSR
     C                   EVAL      £DBG_Pas='P01'
     C     *LIKE         DEFINE    A50_A1        A50_A2
     C     *LIKE         DEFINE    A50_N1        A50_N2
     C                   EVAL      £DBG_Str= 'A50_A2('+A50_A2+')'
     C                                     +' A50_N1('+%CHAR(A50_N1)+')'
     C                                     +' A50_N2('+%CHAR(A50_N2)+')'
      * Expected:
      *  £DBG_Str = 'A50_A2(       ) A50_N2(.00)                       '
     C     £DBG_Str      DSPLY
      *
     C                   ENDSR
