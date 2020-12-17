     FEMPLOYEE  IF   E           K DISK    RENAME(EMPLOYEE:EMPREC)
     **-------------------------------------------------------------------
     D count           S              1  0 inz(0)
     **-------------------------------------------------------------------
     C     *HIVAL        SETLL     EMPLOYEE
     C                   READP     EMPLOYEE
     C                   DOW       NOT %EOF AND COUNT < 5
     C                   EVAL      COUNT += 1
     C     EMPNO         DSPLY
     C                   READP     EMPLOYEE
     C                   ENDDO
     **-------------------------------------------------------------------
     C                   SETON                                          LR
