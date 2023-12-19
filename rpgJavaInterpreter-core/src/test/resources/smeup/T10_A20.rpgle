     D £DBG_Str        S           2560    VARYING
     D A20_N70         S             20P 0
     D A20_N73         S             20P 3
     D A20_N110        S             11P 0
     D A20_N112        S             11P 2
     D A20_N309        S             30P 9

      *-------------------------------------------------------------------------
     C*                   EVAL      £DBG_Pas='P06'
     C                   EVAL      A20_N73=150
     C                   EVAL      A20_N70 =150
     C                   EVAL      A20_N112=150
     C                   EVAL      A20_N110=150
     C                   EVAL      A20_N309=150
     C                   EXSR      SUB_A20_D
     C                   EVAL      £DBG_Str=%CHAR(A20_N73)
     C                                     +%CHAR(A20_N70)
     C                                     +%CHAR(A20_N112)
     C                                     +%CHAR(A20_N110)
     C                                     +%CHAR(A20_N309)


     C     SUB_A20_D     BEGSR
     C                   SUB       -22,67        A20_N73
     C                   SUB       A20_N73       A20_N70
     C                   SUB       3,14          A20_N112
     C                   SUB       A20_N112      A20_N110
     C                   SUB       150,987       A20_N309
     C                   ENDSR

     C     £DBG_Str      DSPLY

      *Expected: 172.670-22146.863-.987000000
      *-------------------------------------------------------------------------