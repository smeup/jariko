     FDUMMYV    CF   E             WORKSTN USROPN
     D MSG             S             50          VARYING
     D A               S              2  0

     C                   EXSR      SR1

     C                   EVAL      MSG='A:'+%CHAR(A)
     C     MSG           DSPLY

      * Subroutine
     C     SR1           BEGSR

     C     A             IFEQ      0

     C                   EXFMT     FMT01
     C                   EVAL      A=A+1

     C                   ENDIF

     C                   EVAL      A=A+1

     C                   ENDSR