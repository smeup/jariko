      * Called twice by READAFTCH3. P1='C' -> CHAIN, otherwise READ. It ends with LR on, so the
      * file is closed at exit: the READ of the second call must start from the first record.
     FEMPLOYEE  IF   E           K DISK
     D OP              S              1
      *
     C     *ENTRY        PLIST
     C                   PARM                    OP
      *
     C                   IF        OP = 'C'
     C     '000030'      CHAIN     EMPLOYEE
     C     EMPNO         DSPLY
     C                   ELSE
     C                   READ      EMPLOYEE
     C     EMPNO         DSPLY
     C                   ENDIF
     C                   SETON                                        LR
