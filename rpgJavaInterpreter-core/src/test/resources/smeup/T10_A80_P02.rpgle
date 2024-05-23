     D NNN             S              6  0 INZ(100000)
     D £DBG_Str        S           2560
     D £DBG_Pas        S             10
     D A80_N1          S              2  0
     DA80_CALL1        PR
     DC1_PAR1                         2  0

    OA* A£.CDOP(CALLP )
     D* CALLP procedura vuota (100.000 volte)
     C                   EVAL      £DBG_Pas='P02'
     C                   EVAL      A80_N1 = 0
     C                   DO        NNN
     C                   CALLP     A80_CALL1(A80_N1)
     C                   ENDDO
     C                   EVAL      £DBG_Str='A80_CALL1'

    RD* Procedura vuota
      *---------------------------------------------------------------------
     PA80_CALL1        B
     DA80_CALL1        PI
     DC1_PAR1                         2  0
      *
     PA80_CALL1        E
      *---------------------------------------------------------------------