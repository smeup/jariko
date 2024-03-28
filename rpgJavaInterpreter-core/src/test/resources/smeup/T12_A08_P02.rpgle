     D £DBG_Str        S              3
      *
     C                   MOVEL     '1'           CHOISE            1
     C                   EXSR      SEZ_T12_A08
      *
     C                   MOVEL     '2'           CHOISE
     C                   EXSR      SEZ_T12_A08
      *
     C                   MOVEL     ' '           CHOISE
     C                   EXSR      SEZ_T12_A08
      *
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                          LR
      *---------------------------------------------------------------
     C     SEZ_T12_A08   BEGSR
      *---------------------------------------------------------------
      *
     C     CHOISE        CASEQ     '1'           CHOISER1
     C     CHOISE        CASEQ     '2'           CHOISER2
     C                   ENDCS
      *
     C                   ENDSR
      *---------------------------------------------------------------
     C     CHOISER1      BEGSR
      *--------------------------------------------------------------*
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'1'
     C                   ENDSR
      *---------------------------------------------------------------
     C     CHOISER2      BEGSR
      *--------------------------------------------------------------*
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'2'
     C                   ENDSR
