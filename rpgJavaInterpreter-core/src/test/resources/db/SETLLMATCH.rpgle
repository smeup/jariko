     FEMPLOYEE  IF   E           K DISK    RENAME(EMPLOYEE:EMPREC)
     **-------------------------------------------------------------------
     D toFind          S              6
     **-------------------------------------------------------------------
     C     *entry        plist
     C                   parm                    toFind
     C     toFind        SETLL     EMPLOYEE
     C                   IF        %EQUAL
     C     'EXACT MATCH' DSPLY
     C                   ELSEIF    %FOUND
     C     'HIGHER MATCH'DSPLY
     ** The following instruction should never return true since
     ** there is no previous read
     C                   ELSEIF    %EOF
     C     'END OF FILE' DSPLY
     C                   ELSE
     C     'NO RECORDS'  DSPLY
     C                   ENDIF
     **-------------------------------------------------------------------
     C                   SETON                                          LR
