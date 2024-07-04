     FDUMMYV    CF   E             WORKSTN USROPN
     D MSG             S             50          VARYING
     D A               S              2  0

     C     A             FOR       A=0 BY 1 TO 3

     C                   EVAL      A=A+1
     C                   EXFMT     FMT01
     C                   EVAL      A=A+1

     C                   ENDFOR

     C                   EVAL      MSG='A:'+%CHAR(A)
     C     MSG           DSPLY