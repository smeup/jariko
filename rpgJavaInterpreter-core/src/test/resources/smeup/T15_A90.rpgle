      **************************************************************************
     D £DBG_Str        S             50                                         Stringa
     D £DBG_Pas        S             10                                         Passo
      **************************************************************************
     D A90_A15         S             20    INZ('Dr. Doolittle')
      **************************************************************************

    OA* A£.BIFN(SCAN  )
     D* %SCAN
     C                   EVAL      £DBG_Pas='P01'
     C                   EVAL      £DBG_Str=
     C                              'MATCH('
     C                                 +%EDITC(%SCAN('oo':A90_A15):'X')+') '
     C                             +'NO_MATCH('
     C                                 +%EDITC(%SCAN('aa':A90_A15):'X')+') '
     C     £DBG_Str      DSPLY
      * Expected value ?


    OA* A£.BIFN(SCAN  )
     D* %SCAN con posizione iniziale
     C                   EVAL      £DBG_Pas='P02'
     C                   EVAL      £DBG_Str=%EDITC(%SCAN('l':A90_A15:9):'X')
     C     £DBG_Str      DSPLY
      * Expected value ?

    OA* A£.BIFN(SCAN  )
     D* %SCAN con posizione iniziale e lunghezza
     C                   EVAL      £DBG_Pas='P03'
     C                   EVAL      £DBG_Str=%EDITC(%SCAN('D':A90_A15:2:4):'X')
     C     £DBG_Str      DSPLY
      * Expected value ?
     C                   Seton                                        LR