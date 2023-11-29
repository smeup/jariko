     D £DBG_Str        S             100
     D A40_P1          S             26P 2
     D A40_P2          S             26P 2
     D A40_P3          S             26P 2
     D A40_P4          S             26P 2

     C*                   EVAL      £DBG_Pas='P04'
     C                   EVAL      A40_P2 = 987.22
     C                   EVAL      A40_P3 = 123456.1
     C                   EVAL      A40_P1 = A40_P3 - A40_P2 + 1
     C                   EVAL      A40_P4 = A40_P1 * A40_P2 + A40_P3 - 1
     C                   EVAL      £DBG_Str = 'A40_P1('+%CHAR(A40_P1)+')'
     C                                     +'A40_P2('+%CHAR(A40_P2)+')'
     C                                     +'A40_P3('+%CHAR(A40_P3)+')'
     C                                     +'A40_P4('+%CHAR(A40_P4)+')'
     C     £DBG_Str      DSPLY

     C* expected: A40_P1(122469.88)A40_P2(987.22)A40_P3(123456.10)A40_P4(121028170.03)