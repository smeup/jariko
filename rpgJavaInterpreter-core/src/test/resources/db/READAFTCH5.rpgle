      * CHAIN that finds nothing leaves the position where it was: after the
      * READ of 000050 (following the chained 000030) a failed CHAIN does not
      * move it, so the next READ returns 000060 (IBM i, RPGPROOF5 T7).
     FEMPLOYEE  IF   E           K DISK
      *
     C     '000030'      CHAIN     EMPLOYEE
     C                   READ      EMPLOYEE
     C     EMPNO         DSPLY
     C     '999999'      CHAIN     EMPLOYEE
     C                   READ      EMPLOYEE
     C     EMPNO         DSPLY
     C                   SETON                                        LR
