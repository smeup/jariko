     D A04_N50_LIM     S              6  0 INZ(100000)
     D A04_N50_CNT     S                   LIKE(A04_N50_LIM)
     D A04_N1          S              3  0 INZ(2)
     D A04_N2          S              3  0 INZ(50)
     D £DBG_Str        S             50          VARYING


     C                   EVAL      A04_N50_CNT = 0
     C                   SELECT
     C     A04_N50_CNT   WHENEQ    0
     C     A04_N1        DO        A04_N2        A04_N4            3 0
     C                   EVAL      A04_N50_CNT = A04_N4
     C                   ENDDO
     C                   ENDSL
     C                   EVAL      £DBG_Str=
     C                                 'A04_N50_CNT('+%CHAR(A04_N50_CNT)+')'
     C                                +'A04_N1('+%CHAR(A04_N1)+')'
     C                                +'A04_N2('+%CHAR(A04_N2)+')'
     C                                +'A04_N4('+%CHAR(A04_N4)+')'
     C     £DBG_Str      DSPLY
