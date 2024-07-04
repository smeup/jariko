     FDUMMYV    CF   E             WORKSTN USROPN
     D MSG             S             50          VARYING
     D A               S              2  0

     C                   MONITOR

     C                   EVAL      A=A+1
     C                   EXFMT     FMT01
     C                   EVAL      A=A+1

     C                   ENDMON

     C                   EVAL      MSG='A:'+%CHAR(A)
     C     MSG           DSPLY