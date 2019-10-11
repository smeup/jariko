    MU* TIMEOUT(12345)
     DA                S              8  0 INZ(5)
     DB                S              8  0 INZ(8)
     D COUNT           S              3  0
     D RESULT          S              8  0 INZ(0)
      *
     C                   FOR       COUNT = 2 DOWNTO 1
     C                   EVAL      RESULT = A + B * 8
     C                   EVAL      RESULT = 0
     C                   ENDFOR
     C                   SETON                                        LR
