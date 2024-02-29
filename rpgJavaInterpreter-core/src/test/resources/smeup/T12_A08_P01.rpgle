     D £DBG_Str        S              1
      *
     C                   MOVEL     '1'           CHOISE            1
     C                   EXSR      SEZ_T12_A08
     C     £DBG_Str      DSPLY
      *
     C                   MOVEL     '2'           CHOISE            1
     C                   EXSR      SEZ_T12_A08
     C     £DBG_Str      DSPLY
      *
     C     *IN34         CABEQ     *OFF          G4MAIN
     C     'IN34=ON'     DSPLY
     C     G4MAIN        TAG
      *
     C                   SETON                                          LR
      *
     C     SEZ_T12_A08   BEGSR
     C     CHOISE        CASEQ     '1'           CHOISER1
     C     CHOISE        CASEQ     '2'           CHOISER2
     C                   ENDCS
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Errori programma MULANGT12 sezione A08 - ROUTINE 1
      *--------------------------------------------------------------*
     C     CHOISER1      BEGSR
     c                   EVAL      £DBG_Str='1'
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Errori programma MULANGT12 sezione A08 - ROUTINE 2
      *--------------------------------------------------------------*
     C     CHOISER2      BEGSR
     c                   EVAL      £DBG_Str='2'
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Errori programma MULANGT12 sezione A08 - ROUTINE 3
      *--------------------------------------------------------------*
     C     CHOISER3      BEGSR
      *
     C     *IN35         CABEQ     *OFF          G5MAIN
     C     'IN35=ON'     DSPLY
     C     G5MAIN        TAG
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Errori programma MULANGT12 sezione A08 - ROUTINE 3
      *--------------------------------------------------------------*
     C     CHOISER4      BEGSR
      *
     C                   SELECT
     C     £CRNA         WHENEQ    'ZZ'
     C                   CALL      'CALLDEFV2'
     C                   PARM                    £CRNB             2
      *
     C                   OTHER
     C                   CALL      'CALLDEFV2'
     C                   PARM                    £CRNA             2
     C                   ENDSL
      *
     C                   ENDSR
