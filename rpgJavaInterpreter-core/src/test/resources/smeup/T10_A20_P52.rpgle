     D £DBG_Str        S            150          VARYING

     D A20_N70         S             20P 0
     D A20_N73         S             20P 3
     D A20_N110        S             11P 0
     D A20_N112        S             11P 2
     D A20_N309        S             30P 9

     C                   EXSR      SUB_A20_I
     C                   EVAL      £DBG_Str='A20_N73('+%CHAR(A20_N73)+')'
     C                                    +' A20_N70('+%CHAR(A20_N70)+')'
     C                                    +' A20_N112('+%CHAR(A20_N112)+')'
     C                                    +' A20_N110('+%CHAR(A20_N110)+')'
     C                                    +' A20_N309('+%CHAR(A20_N309)+')'

     C     £DBG_Str      DSPLY

      *---------------------------------------------------------------------
     C     SUB_A20_I     BEGSR
      *
     C                   SETOFF                                       22
     C                   SETON                                        23
     C  N2225            DIV       11            A20_N73
     C  N22              MVR                     A20_N70
     C   23100           DIV       9             A20_N112
     C   23              MVR                     A20_N110
     C     A20_N112      DIV       A20_N73       A20_N309
     C                   ENDSR
