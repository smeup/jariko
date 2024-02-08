      **************************************************************************
     D £DBG_Str        S             50                                         Stringa
     D £DBG_Pas        S             50                                         Stringa
      *
      * T10_A60
      * with QUALIFIED keyword
     D A60PDS          DS                  QUALIFIED
     D  A60PGM                       10
     D  A60P4                        10
     D  A60P5                        50
      * without QUALIFIED keyword
     D A60_PDS         DS
     D  A60_PGM                      10
     D  A60_P4                       10
     D  A60_P5                       50
      *
     C                   EXSR      SEZ_T10_A60
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
    RD* Errori programma MULANGT10 sezione A60
      *--------------------------------------------------------------*
     C     SEZ_T10_A60   BEGSR
      *
    OA* A£.CDOP(CALL;DS)
     D* CALL a PGM in DS con parametri in input, risultato no indicatore
     C                   EVAL      *IN37=*OFF
     C                   EVAL      £DBG_Pas='P04'
     C                   EVAL      A60PDS.A60P4='4'
     C                   EVAL      A60PDS.A60P5=' '
     C                   EVAL      A60PDS.A60PGM='MULANGTC30'
     C                   CALL      A60PDS.A60PGM
     C                   PARM                    A60PDS.A60P4
     C                   PARM                    A60PDS.A60P5
     C                   EVAL      £DBG_Str='CALL('+A60PDS.A60PGM+', '
     C                                     +%CHAR(A60PDS.A60P4)+', '
     C                                     +%CHAR(*IN37)
     C                                     +') '
     C     £DBG_Str      DSPLY
      *
    OA* A£.CDOP(CALL;DS)
     D* CALL a PGM in DS con parametri in input, risultato si indicatore
     C                   EVAL      *IN37=*OFF
     C                   EVAL      £DBG_Pas='P05'
     C                   EVAL      A60PDS.A60P4='5'
     C                   EVAL      A60PDS.A60P5=' '
     C                   EVAL      A60PDS.A60PGM='MULANGTC30'
     C                   CALL      A60PDS.A60PGM                        37
     C                   PARM                    A60PDS.A60P4
     C                   PARM                    A60PDS.A60P5
     C                   EVAL      £DBG_Str='CALL('+A60PDS.A60PGM+', '
     C                                     +%CHAR(A60PDS.A60P4)+', '
     C                                     +%CHAR(*IN37)
     C                                     +') '
     C     £DBG_Str      DSPLY
      *
    OA* A£.CDOP(CALL;DS)
     D* CALL a PGM in DS con parametri in input, risultato no indicatore
     C                   EVAL      *IN37=*OFF
     C                   EVAL      £DBG_Pas='P06'
     C                   EVAL      A60_P4='6'
     C                   EVAL      A60_P5=' '
     C                   EVAL      A60_PGM='MULANGTC30'
     C                   CALL      A60_PGM
     C                   PARM                    A60_P4
     C                   PARM                    A60_P5
     C                   EVAL      £DBG_Str='CALL('+A60_PGM+', '
     C                                     +%CHAR(A60_P4)+', '
     C                                     +%CHAR(*IN37)
     C                                     +') '
     C     £DBG_Str      DSPLY
      *
    OA* A£.CDOP(CALL;DS)
     D* CALL a PGM in DS con parametri in input, risultato si indicatore
     C                   EVAL      *IN37=*OFF
     C                   EVAL      £DBG_Pas='P07'
     C                   EVAL      A60_P4='7'
     C                   EVAL      A60_P5=' '
     C                   EVAL      A60_PGM='MULANGTC30'
     C                   CALL      A60_PGM                              37
     C                   PARM                    A60_P4
     C                   PARM                    A60_P5
     C                   EVAL      £DBG_Str='CALL('+A60_PGM+', '
     C                                     +%CHAR(A60_P4)+', '
     C                                     +%CHAR(*IN37)
     C                                     +') '
     C     £DBG_Str      DSPLY
     C                   ENDSR
