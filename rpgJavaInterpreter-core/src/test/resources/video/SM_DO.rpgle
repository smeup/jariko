     FDUMMYV    CF   E             WORKSTN USROPN
     D MSG             S             50          VARYING
     D A               S              2  0
     D I               S              2  0

     C     0             DO        10            I

     C     I             IFEQ      5

     C                   EVAL      A=A+1
     C                   EXFMT     FMT01

     C                   ENDIF

     C                   ENDDO

     C                   EVAL      MSG='A:'+%CHAR(A)
     C     MSG           DSPLY