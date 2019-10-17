     D A1              S              5A   INZ('AAAA') VARYING
     D A2              S             1 A   INZ('AAAAAAAA') VARYING
     D A3              S             2 A   INZ('AAAAAAAAAAAA') VARYING
     D B1              S              5A   INZ('BBBBB')
     D B2              S             1 A   INZ('BBBBBBBBBB')
     D B3              S             2 A   INZ('BBBBBBBBBBBBBBBBBBBB')
     D C1              S              5A   INZ('CCC') VARYING
     D C2              S             1 A   INZ('CCCCCCC') VARYING
     D C3              S             2 A   INZ('CCCCCCCCCCCCCCCCC') VARYING
     C     A1            DSPLY
     C     A2            DSPLY
     C     A3            DSPLY
     C     B1            DSPLY
     C     B2            DSPLY
     C     B3            DSPLY
     C     C1            DSPLY
     C     C2            DSPLY
     C     C3            DSPLY
     C                   MOVEL     A1            B2
     C                   MOVEL     A1            B2
     C                   MOVEL     A2            B3
     C                   MOVEL     A3            B1
     C                   MOVEL     B1            C3
     C                   MOVEL     B2            C1
     C                   MOVEL     B3            C2
     C                   MOVEL     C1            A3
     C                   MOVEL     C2            A1
     C                   MOVEL     C3            A2
     C     A1            DSPLY
     C     A2            DSPLY
     C     A3            DSPLY
     C     B1            DSPLY
     C     B2            DSPLY
     C     B3            DSPLY
     C     C1            DSPLY
     C     C2            DSPLY
     C     C3            DSPLY
     C                   SETON                                            LR