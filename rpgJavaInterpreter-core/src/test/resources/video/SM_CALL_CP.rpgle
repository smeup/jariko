     D MSG             S             50          VARYING
     D A               S              2  0

     C                   EVAL      A=A+1

     C                   CALL      'SM_CP'
     C                   PARM                    A

     C                   EVAL      A=A+1

     C                   EVAL      MSG='A:'+%CHAR(A)
     C     MSG           DSPLY