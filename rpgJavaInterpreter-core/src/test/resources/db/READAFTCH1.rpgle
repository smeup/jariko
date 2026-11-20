      * READ right after a successful CHAIN: reads the record following the chained one
      * (access path order). EMPNO 000030 is followed by 000050.
     FEMPLOYEE  IF   E           K DISK
      *
     C     '000030'      CHAIN     EMPLOYEE
     C     EMPNO         DSPLY
     C                   READ      EMPLOYEE
     C     EMPNO         DSPLY
     C                   SETON                                        LR
