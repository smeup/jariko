     FDUMMYV    CF   E             WORKSTN USROPN
     D MSG             S             50          VARYING
     D A               S              2  0
     D I               S              2  0

     C     I             FOR       I=0 BY 1 TO 1

     C                   SELECT

     C     I             WHENEQ    0
     C                   EVAL      A=A+1

     C     I             WHENEQ    1
     C                   EVAL      A=A+1
     C                   EXFMT     FMT01
     C                   EVAL      A=A+1

     C     I             WHENEQ    2
     C                   EVAL      A=A+1

     C                   OTHER
     C                   EVAL      A=A+1

     C                   ENDSL

     C                   ENDFOR

     C                   EVAL      MSG='A:'+%CHAR(A)
     C     MSG           DSPLY