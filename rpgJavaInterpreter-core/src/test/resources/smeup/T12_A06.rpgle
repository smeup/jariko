     D £DBG_Pas        S              3
     D £DBG_Str        S            100
     D A06_N20A        S              2  0 INZ(10)
     D A06_N20B        S                   LIKE(A06_N20A)
      *
     C                   EVAL      £DBG_Pas='P03'
     C                   EVAL      £DBG_Str=''
     C                   EXSR      SEZ_A06_R03
     C     £DBG_Str      DSPLY
     C                   SETON                                        LR
      *
     C     SEZ_A06_R03   BEGSR
     C                   SETOFF                                       373839
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'CABNE(('+
     C                               %CHAR(A06_N20A)+'=/='+%CHAR(A06_N20B)+')=>'
     C     A06_N20A      CABNE     A06_N20B      A06_J03              373839
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'NO_'
     C     A06_J03       TAG
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+'CABNE; '+
     C                                  'Hi='+%CHAR(*IN37)+
     C                                  '; Lo='+%CHAR(*IN38)+
     C                                  '; Eq='+%CHAR(*IN39)+')'
     C                   ENDSR