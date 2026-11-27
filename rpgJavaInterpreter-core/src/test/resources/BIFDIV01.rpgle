     V* ==============================================================
     D* %DIV: integer quotient, truncated toward zero
     V* ==============================================================
     D R1              S              5  0
     D R2              S              5  0
     D R3              S              5  0
     D A               S              5  0 INZ(17)
     D B               S              5  0 INZ(5)
     D MSG             S             20
     C                   EVAL      R1 = %DIV(A:B)
     C                   EVAL      R2 = %DIV(-17:B)
     C                   EVAL      R3 = %DIV((A-2)*2:B)
     C                   EVAL      MSG = 'R1=' + %CHAR(R1)
     C     MSG           DSPLY
     C                   EVAL      MSG = 'R2=' + %CHAR(R2)
     C     MSG           DSPLY
     C                   EVAL      MSG = 'R3=' + %CHAR(R3)
     C     MSG           DSPLY
     C                   SETON                                        LR
