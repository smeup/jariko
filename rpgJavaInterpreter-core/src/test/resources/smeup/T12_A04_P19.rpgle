     D A04_N50_LIM     S              6  0 INZ(100000)
     D A04_N50_CNT     S                   LIKE(A04_N50_LIM)
     D A04_STR         S              2  0
     D A04_CNT         S              2  0
     D A04_END         S              2  0
     D £DBG_Str        S            100          VARYING

     C                   EVAL      A04_STR = 1
     C                   EVAL      A04_END = 10
     C     A04_STR       DO        A04_END       A04_CNT                        -commento
     C                   EVAL      A04_N50_CNT = A04_CNT
     C                   END
     C                   EVAL      £DBG_Str='DO_1('
     C                                +'A04_N50_CNT('+%CHAR(A04_N50_CNT)+') '
     C                                +'A04_STR('+%CHAR(A04_STR)+') '
     C                                +'A04_END('+%CHAR(A04_END)+') '
     C                                +'A04_CNT('+%CHAR(A04_CNT)+')) '
      *
     C                   DO        15            A04_CNT
     C                   EVAL      A04_N50_CNT = A04_CNT
     C                   END
     C                   EVAL      £DBG_Str=%TRIMR(£DBG_Str)+' DO_2('
     C                                +'A04_N50_CNT('+%CHAR(A04_N50_CNT)+') '
     C                                +'A04_CNT('+%CHAR(A04_CNT)+'))'
     C     £DBG_Str      DSPLY
