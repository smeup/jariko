     D £DBG_Str        S             2

     D F$LIB           S                   LIKE(U$LIB )
     D U$LIB           S                   LIKE($IR1LB)
     C     £INIZI        BEGSR
     C                   CLEAR                   $Ir1LB           10
     C                   ENDSR

     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY
