     D MSG             S             50          VARYING
     D A               S              2  0

     C                   EVAL      A=A+1

     C                   CALL      'SM_P_PLAIN'
     C                   PARM                    A

     C                   EVAL      A=A+4

     C                   EVAL      MSG='A:'+%CHAR(A)
     C     MSG           DSPLY