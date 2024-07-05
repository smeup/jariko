     FDUMMYV    CF   E             WORKSTN USROPN
     D MSG             S             50          VARYING
     D A               S              2  0
     D B               S              2  0

     C     B             DO        2             A

     C                   EXFMT     FMT01

     C                   ENDDO

     C                   EVAL      MSG='A:'+%CHAR(A)
     C     MSG           DSPLY