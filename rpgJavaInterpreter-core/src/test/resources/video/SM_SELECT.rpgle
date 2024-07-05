     FDUMMYV    CF   E             WORKSTN USROPN
     D MSG             S             50          VARYING
     D A               S              2  0

     C                   SELECT

     C     A             WHENEQ    0
     C                   EVAL      A=A+1
     C                   EXFMT     FMT01
     C                   EVAL      A=A+1

     C     A             WHENEQ    1
     C                   EVAL      A=A+1

     C     A             WHENEQ    2
     C                   EVAL      A=A+1

     C                   OTHER
     C                   EVAL      A=0

     C                   ENDSL

     C                   EVAL      MSG='A:'+%CHAR(A)
     C     MSG           DSPLY