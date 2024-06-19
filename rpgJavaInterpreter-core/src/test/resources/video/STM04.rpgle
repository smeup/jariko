     D MSG             S             50          VARYING
     D A               S              2  0
     D B               S              2  0

     C                   IF         A<=0
     C                   EVAL       A=A+1
     C                   EVAL       MSG='A:'+%CHAR(A)
     C     MSG           DSPLY

     C                   IF         B<=0
     C                   EVAL       A=A+1
     C                   EVAL       B=B+1
     C                   EVAL       MSG='B:'+%CHAR(B)
     C     MSG           DSPLY
     C                   ENDIF

     C                   ENDIF