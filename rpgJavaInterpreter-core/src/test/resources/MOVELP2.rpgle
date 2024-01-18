     D A10_A1          S              5A   INZ('AAAA') VARYING
     D A10_A2          S             10A   INZ('AAAAAAAA') VARYING
     D A10_A3          S             20A   INZ('AAAAAAAAAAAA') VARYING
     D A10_B1          S              5A   INZ('BBBBB')
     D A10_B2          S             10A   INZ('BBBBBBBBBB')
     D A10_B3          S             20A   INZ('BBBBBBBBBBBBBBBBBBBB')
     D A10_C1          S              5A   INZ('CCC') VARYING
     D A10_C2          S             10A   INZ('CCCCCCC') VARYING
     D A10_C3          S             20A   INZ('CCCCCCCCCCCCCCCCC') VARYING
      *
     C                   MOVEL(P)  A10_A1        A10_B2
     C     A10_B2        DSPLY
      *
     C                   MOVEL(P)  A10_A2        A10_B3
     C     A10_B3        DSPLY
      *
     C                   MOVEL(P)  A10_A3        A10_B1
     C     A10_B1        DSPLY
      *
     C                   MOVEL(P)  A10_B1        A10_C3
     C     A10_C3        DSPLY
      *
     C                   MOVEL(P)  A10_B2        A10_C1
     C     A10_C1        DSPLY
      *
     C                   MOVEL(P)  A10_B3        A10_C2
     C     A10_C2        DSPLY
      *
     C                   MOVEL(P)  A10_C1        A10_A3
     C     A10_A3        DSPLY
      *
     C                   MOVEL(P)  A10_C2        A10_A1
     C     A10_A1        DSPLY
      *
     C                   MOVEL(P)  A10_C3        A10_A2
     C     A10_A2        DSPLY
      *
     C                   SETON                                        LR