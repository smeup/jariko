     D A30_AR1         S             20    DIM(10)
     D A30_AR3         S             20    DIM(%ELEM(A30_AR1))
     D £DBG_Str        S            150          VARYING

     C                   EVAL      A30_AR3(01) = 'AAAAAAAAAAAAAAAAAAAA'
     C                   EVAL      A30_AR3(02) = 'BBBBBBBBBBBBBBBBBBBB'
     C                   EVAL      A30_AR3(03) = 'CCCCCCCCCCCCCCCCCCCC'
     C                   EVAL      A30_AR3(04) = 'DDDDDDDDDDDDDDDDDDDD'
     C                   EVAL      A30_AR3(05) = 'EEEEEEEEEEEEEEEEEEEE'
     C                   EVAL      A30_AR3(06) = 'FFFFFFFFFFFFFFFFFFFF'
     c                   EVAL      £DBG_Str=A30_AR3(01)+A30_AR3(02)+A30_AR3(03)
     C                                     +A30_AR3(04)+A30_AR3(05)+A30_AR3(06)
     C     £DBG_Str      DSPLY
