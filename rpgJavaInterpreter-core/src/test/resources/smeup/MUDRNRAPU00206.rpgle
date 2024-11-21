     D £DBG_Str        S             2
     C                   Z-ADD     UDATE         UDATE1            4 0
     C                   Z-ADD     UYEAR         UYEAR1            4 0
     C                   Z-ADD     UMONTH        UMONTH1           4 0
     C                   Z-ADD     UDAY          UDAY1             4 0

     C                   EVAL      £DBG_Str='ok'
      * TODO fix ZADD implementation because UDATE when assigned to UDATE1 must be truncated to 4 digits.
      * See in AS400 the result of this operation.
     C     UDATE1        DSPLY     
     C     UYEAR1        DSPLY     
     C     UMONTH1       DSPLY     
     C     UDAY1         DSPLY
     C     £DBG_Str      DSPLY