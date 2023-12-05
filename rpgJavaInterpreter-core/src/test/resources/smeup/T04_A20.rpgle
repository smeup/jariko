     D £DBG_Str        S           2560
     D A20_A10         S             10
     D A20_P1          S             10    INZ('MULANGT04')
     D A20_P2          S              2  0
     D A20_P3          S             50
      *
     C                   CLEAR                   A20_P3
     C                   CALL      'MULANGTB10'
     C                   PARM                    A20_P1
     C                   PARM      1             A20_P2
     C                   PARM                    A20_P3
     C                   EVAL      £DBG_Str='CALL_1('+A20_P1+', '
     C                                     +%CHAR(A20_P2)
     C                                     +', '+A20_P3+') '
      *
     C                   CLEAR                   A20_P3
     C                   EVAL      A20_A10='MULANGTB10'
     C                   CALL      A20_A10
     C                   PARM                    A20_P1
     C                   PARM      3             A20_P2
     C                   PARM                    A20_P3
     C                   EVAL      £DBG_Str=%TRIMR(£DBG_Str)
     C                                     +' CALL_2('+A20_P1+', '
     C                                     +%CHAR(A20_P2)
     C                                     +', '+A20_P3+') '
      *
      * Expect 'CALL_1(MULANGT04 , 1, MULANGTB10: MULANGT04 chiamata 1                  ) CALL_2(MULANGT04 , 3, MULANGTB10: MULANGT04 chiamata 1                  )'
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR