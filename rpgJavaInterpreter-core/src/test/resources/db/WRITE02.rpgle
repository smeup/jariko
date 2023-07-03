     FEMPLVIEW  UF A E           K DISK
     D EXPECTED        S              6
      *--------------------------------------------------------------------------------------------*
      *WRITING
     C                   EVAL      EMPNO='000000'
     C                   EVAL      FIRSTNME='JOHN'
     C                   EVAL      MIDINIT='1'
     C                   EVAL      LASTNAME='ROSS'
     C                   EVAL      WORKDEPT='012'
     C                   WRITE     EMPLOYEE
     C                   EVAL      EMPNO='000001'
     C                   EVAL      FIRSTNME='MARC'
     C                   WRITE     EMPLOYEE
      *--------------------------------------------------------------------------------------------*
      *READING
     C     KL            KLIST
     C                   KFLD                    EXPECTED
     C                   EVAL       EXPECTED = '000000'
     C     KL            SETLL      EMPLOYEE
    MU* VAL1(EXPECTED) VAL2(EMPNO) COMP(EQ)
     C     KL            READE      EMPLOYEE
      *--------------------------------------------------------------------------------------------*
     C                   SETON                                        LR