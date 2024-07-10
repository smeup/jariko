     D MSG             S             50          VARYING
     D A               S              2  0

     C                   EXFMT     FMT01

     C                   CALL      'SM_ADD'
     C                   PARM                    A

     C                   EXFMT     FMT01

     C                   EVAL      MSG='A:'+%CHAR(A)
     C     MSG           DSPLY