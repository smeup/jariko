      * CHAIN that finds nothing on a file never read: the position is unchanged,
      * so the following READ returns the first record (IBM i, RPGPROOF5 T7).
     FEMPLOYEE  IF   E           K DISK
      *
     C     '999999'      CHAIN     EMPLOYEE
     C                   READ      EMPLOYEE
     C     EMPNO         DSPLY
     C                   SETON                                        LR
