     V* ==============================================================
     D* 02/07/24
     D* Test the stack trace read behavior by placing EXFMT in certain
     D* points
     V* ==============================================================

     FSTKR01V   CF   E             WORKSTN USROPN
     D MSG             S             50          VARYING
     D A               S              2  0
     D B               S              2  0

     C                   EVAL      A=A+1
     C                   EVAL      B=B+1

     C                   EXFMT     FMT01

     C                   EVAL      A=A+2
     C                   EVAL      B=B+2

     C                   EVAL      MSG='A:'+%CHAR(A)
     C     MSG           DSPLY
     C                   EVAL      MSG='B:'+%CHAR(B)
     C     MSG           DSPLY