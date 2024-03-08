     D A30_C04         C                   CONST(6)
     D A30_AR4         S             20    DIM(A30_C04)
     D £DBG_Str        S             150         VARYING

     C                   EVAL      A30_AR4(01) = 'AAAAAAAAAAAAAAAAAAAA'
     C                   EVAL      A30_AR4(02) = 'BBBBBBBBBBBBBBBBBBBB'
     C                   EVAL      A30_AR4(03) = 'CCCCCCCCCCCCCCCCCCCC'
     C                   EVAL      A30_AR4(04) = 'DDDDDDDDDDDDDDDDDDDD'
     C                   EVAL      A30_AR4(05) = 'EEEEEEEEEEEEEEEEEEEE'
     C                   EVAL      A30_AR4(06) = 'FFFFFFFFFFFFFFFFFFFF'
     c                   EVAL      £DBG_Str=A30_AR4(01)+A30_AR4(02)+A30_AR4(03)
     C                                     +A30_AR4(04)+A30_AR4(05)+A30_AR4(06)
     C     £DBG_Str      DSPLY
