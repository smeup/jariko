     FDUMMYV    CF   E             WORKSTN USROPN
     D MSG             S             50          VARYING
     D A               S              2  0
     D B               S              2  0

     C     A             DOUEQ     1

     C                   EVAL      A=A+1
     C                   EXFMT     FMT01
     C                   EVAL      B=B+1

     C                   ENDDO

     C                   EVAL      MSG='A:'+%CHAR(A)
     C     MSG           DSPLY
     C                   EVAL      MSG='B:'+%CHAR(B)
     C     MSG           DSPLY