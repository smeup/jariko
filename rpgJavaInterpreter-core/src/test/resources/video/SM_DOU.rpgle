     FDUMMYV    CF   E             WORKSTN USROPN
     D MSG             S             50          VARYING
     D A               S              2  0
     D X               S              2  0

     C                   DOU       A=1

     C                   EVAL      A=A+1
     C                   EXFMT     FMT01
     C                   EVAL      X=X+1

     C                   ENDDO

     C                   EVAL      MSG='A:'+%CHAR(A)
     C     MSG           DSPLY
     C                   EVAL      MSG='X:'+%CHAR(X)
     C     MSG           DSPLY