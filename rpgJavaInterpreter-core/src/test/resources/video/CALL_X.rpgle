     D MSG             S             50          VARYING
     D A               S              2  0

     C                   CALL      'C_EAE'
     C                   PARM                    A

     C                   EVAL      MSG='A:'+%CHAR(A)
     C     MSG           DSPLY