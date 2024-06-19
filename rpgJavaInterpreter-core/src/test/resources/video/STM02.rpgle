     D MSG             S             50          VARYING
     D A               S              2  0
     D B               S              2  0
      * First DOWEQ loop
     C     A             DOWEQ      10
     C                   EVAL       A=A+1
     C                   ENDDO
     C                   EVAL       MSG='A:'+%CHAR(A)
     C     MSG           DSPLY
      * Second DOWEQ loop
     C     B             DOWEQ      10
     C                   EVAL       B=B+1
     C                   ENDDO
     C                   EVAL       MSG='B:'+%CHAR(B)
     C     MSG           DSPLY
