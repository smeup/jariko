     D £DBG_Str        S              1
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
     C                   SETON                                          LR
      *---------------------------------------------------------------
     C     SEZ_T12_A08   BEGSR
      *---------------------------------------------------------------
      *
     C     CHOISE        CASEQ     '1'           CHOISER1
     C     CHOISE        CASEQ     '2'           CHOISER2
     C                   CAS                     CHOISER3
     C                   ENDCS
      *
     C     £DBG_Str      DSPLY
      *
     C                   ENDSR
      *---------------------------------------------------------------
     C     CHOISER1      BEGSR
      *--------------------------------------------------------------*
     c                   EVAL      £DBG_Str='1'
     C                   ENDSR
      *---------------------------------------------------------------
     C     CHOISER2      BEGSR
      *--------------------------------------------------------------*
     c                   EVAL      £DBG_Str='2'
     C                   ENDSR
      *---------------------------------------------------------------
     C     CHOISER3      BEGSR
      *--------------------------------------------------------------*
     C                   EVAL      £DBG_Str='3'
     C                   ENDSR
