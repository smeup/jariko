     FEMPLOYEE  IF   E           K DISK    RENAME(EMPLOYEE:EMPREC)
     D toFind          S              6
     C     *entry        PLIST
     C                   PARM                    toFind
     C     toFind        SETLL     EMPLOYEE                               37
     C                   IF        *IN37
     C     'EQ=1'        DSPLY
     C                   ELSE
     C     'EQ=0'        DSPLY
     C                   ENDIF
     C                   SETON                                          LR
