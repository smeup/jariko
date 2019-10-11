    MU* TIMEOUT(1)
     DA                S              8  0 INZ(5)
     DB                S              8  0 INZ(8)
     D COUNT           S              3  0
     D RESULT          S              8  0 INZ(0)
      *
     C                   FOR       COUNT = 200 DOWNTO 1
     C                   EVAL      RESULT = A + B * 8
     C                   EVAL      RESULT = 0
     C                   ENDFOR
    MU* TIMEOUT(234)
     C                   SETON                                        LR
