     FEMPLOYEE  IF   E           K DISK
     D PETER           S             12    INZ('PETER')
      *--------------------------------------------------------------------------------------------*
      *WRITING
     C                   EVAL      EMPNO='000000'
     C                   EVAL      FIRSTNME='JOHN'
     C                   EVAL      MIDINIT='1'
     C                   EVAL      LASTNAME='ROSS'
     C                   EVAL      WORKDEPT='012'
     C                   WRITE     EMPLOYEE
     C     KL            KLIST
     C                   KFLD                    EMPNO
    MU* VAL1('JOHN') VAL2(FIRSTNME) COMP(EQ)
     C     KL            READE     EMPLOYEE
      *CHANGE FIRST NAME
     C                   EVAL      FIRSTNME=PETER
     C                   UPDATE    EMPLOYEE
     C                   EVAL      FIRSTNME=''
    MU* VAL1(PETER) VAL2(FIRSTNME) COMP(EQ)
     C     KL            READE     EMPLOYEE
      *--------------------------------------------------------------------------------------------*
     C                   SETON                                        LR