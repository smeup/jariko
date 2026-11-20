      * CLOSE + OPEN resets the file position: the first READ after reopening returns the
      * first record (EMPNO 000010), even if a CHAIN was done before the CLOSE.
     FEMPLOYEE  IF   E           K DISK
      *
     C     '000030'      CHAIN     EMPLOYEE
     C     EMPNO         DSPLY
     C                   CLOSE     EMPLOYEE
     C                   OPEN      EMPLOYEE
     C                   READ      EMPLOYEE
     C     EMPNO         DSPLY
     C                   SETON                                        LR
