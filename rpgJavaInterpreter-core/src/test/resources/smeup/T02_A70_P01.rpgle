     D £DBG_Str        S             15
     D £DBG_Pas        S             10
      *
     D NIDX            S              5  0
     D XCF             S             15
      *
     C                   EVAL      £DBG_Str=' '
     C                   EVAL      NIDX=£MUGTP('B')
     C                   EVAL      £DBG_Str=%CHAR(NIDX)
     C     £DBG_Str      DSPLY
      /COPY QPROGEN,£MULANG_P
