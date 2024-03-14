     D A20_D1          S              3  0 INZ(32)
     D A20_D2          S              3  1 INZ(0.6)
     D A20_D3          S              2  0 INZ(25)
     D A20_D4          S              2  0 INZ(11)
     D £DBG_Str        S             50          VARYING

     C     A20_D1        DIV       A20_D2        A20_D7           11 2
     C                   MVR                     A20_D8            5 3
     C     A20_D3        DIV       A20_D4        A20_D9            2 0
     C                   MVR                     A20_D0            2 0
     C                   EVAL      £DBG_Str='A20_D7('+%CHAR(A20_D7)+')'
     C                                    +' A20_D8('+%CHAR(A20_D8)+')'
     C                                    +' A20_D9('+%CHAR(A20_D9)+')'
     C                                    +' A20_D0('+%CHAR(A20_D0)+')'
     C     £DBG_Str      DSPLY