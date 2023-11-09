     D  fix5A          S              5A   INZ('AAAAA')
     D  fix5B          S              5A   INZ('BB')
     D  fix10A         S             10A   INZ('PQRST     ')
     D  fix10B         S             10A   INZ('PQRSTUVWXY')
      *
     C                   MOVE(P)   fix5a         fix10A
     C                   MOVE(P)   fix10A        fix10B
     C                   MOVE(P)   fix5b         fix10A
     C     fix10A        DSPLY
     C     fix10B        DSPLY
     C                   SETON                                        LR