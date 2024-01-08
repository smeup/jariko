     FEMPLOYEE  IF   E           K DISK    UsrOpn
      **************************************************************************
     D £DBG_Str        S           2560                                         Stringa
     D £DBG_Pas        S             10                                         Passo
      **************************************************************************
     D* All files are open by default even if the UsrOpn parm is not defined
     C                   EVAL      £DBG_Pas='P01'
     C                   EVAL      £DBG_Str='TEST_%OPEN:'
     C                   IF        %OPEN(EMPLOYEE)
     C                   EVAL      £DBG_Str='OPENED'
     C                   ELSE
     C                   EVAL      £DBG_Str='CLOSED'
     C                   ENDIF
     C     £DBG_Str      DSPLY
      *
     C                   CLOSE     EMPLOYEE
     C                   IF        NOT %OPEN(EMPLOYEE)
     C                   EVAL      £DBG_Str='CLOSED'
     C     £DBG_Str      DSPLY
     C                   ENDIF
      *
     C                   OPEN      EMPLOYEE
     C                   IF        %OPEN(EMPLOYEE)
     C                   EVAL      £DBG_Str='OPENED'
     C     £DBG_Str      DSPLY
     C                   ENDIF
      *
     C                   SETON                                        LR