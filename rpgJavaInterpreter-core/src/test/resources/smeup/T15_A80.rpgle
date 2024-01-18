      **************************************************************************
     D £DBG_Str        S           2560                                         Stringa
     D £DBG_Pas        S             10                                         Passo
      **************************************************************************
     D A80_A08         S             10    INZ('Monaco  ')
     D A80_A10         S             10    INZ('Toronto   ')
     D A80_N51         S              5  1
     D A80_N72         S              7  2
      **************************************************************************

     C*                   EVAL      £DBG_Pas='P02'
     C*                   EVAL      £DBG_Str=%EDITC(%LEN(%TRIMR(A80_A10)):'X')

    OA* A£.BIFN(LEN  )
     D* %LEN di numero
     C                   EVAL      £DBG_Pas='P04'
     C                   EVAL      £DBG_Str=%EDITC(%LEN(A80_N72):'X')

    OA* A£.BIFN(LEN  )
     D* Operazioni con %LEN
     C                   EVAL      £DBG_Pas='P05'

     C                   EVAL      £DBG_Str=
     C                              'NUM'+%EDITC(%LEN(A80_N72*A80_N51):'X')+') '
     C                             +'STR('+%EDITC(%LEN(A80_A10+A80_A08):'X')+')'