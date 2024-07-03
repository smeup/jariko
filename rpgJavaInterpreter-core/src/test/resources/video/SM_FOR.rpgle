     FDUMMYV    CF   E             WORKSTN USROPN
     D MSG             S             50          VARYING
     D A               S              2  0
     D I               S              2  0

     C     I             FOR       I=1 BY 1 TO 10

     C     I             IFEQ      5

     C                   EXFMT     FMT01
     C                   EVAL      A=A+1

     C                   ENDIF

     C                   EVAL      A=A+1

     C                   ENDFOR

     C                   EVAL      MSG='A:'+%CHAR(A)
     C     MSG           DSPLY