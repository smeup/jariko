     FEMPLOYEE  IF   E           K DISK    UsrOpn
      **************************************************************************
     D STR             S             10
      **************************************************************************
     D* All files are open by default even if the UsrOpn parm is not defined
     C                   IF        %OPEN(EMPLOYEE)
     C                   EVAL      STR='OPENED'
     C                   ELSE
     C                   EVAL      STR='CLOSED'
     C                   ENDIF
     C     STR           DSPLY
      *
     C                   CLOSE     EMPLOYEE
     C                   IF        NOT %OPEN(EMPLOYEE)
     C                   EVAL      STR='CLOSED'
     C     STR           DSPLY
     C                   ENDIF
      *
     C                   OPEN      EMPLOYEE
     C                   IF        %OPEN(EMPLOYEE)
     C                   EVAL      STR='OPENED'
     C     STR           DSPLY
     C                   ENDIF
      *
     C                   SETON                                        LR