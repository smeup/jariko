     FDUMMYV    CF   E             WORKSTN USROPN
     D MSG             S             50          VARYING
     D A               S              2  0
     D B               S              2  0
     D C               S              2  0

     C     B             DOWLT     2

     C                   EXFMT     FMT01
     C                   EVAL      A=A+1

     C                   IF        B=1

     C                   EXFMT     FMT01
     C                   EVAL      A=A+1

     C                   ENDIF

     C                   FOR      C=0 BY 1 TO 1

     C                   EXFMT     FMT01
     C                   EVAL      A=A+1

     C                   ENDFOR

     C                   EVAL      B=B+1

     C                   ENDDO

     C                   EVAL      MSG='A:'+%CHAR(A)
     C     MSG           DSPLY