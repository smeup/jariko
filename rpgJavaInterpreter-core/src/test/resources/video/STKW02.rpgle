     FSTKW02V   CF   E             WORKSTN USROPN
     D MSG             S             50          VARYING
     D A               S              2  0
     D B               S              2  0

     C                   EXFMT      FMT01

     C                   IF         A<=0
     C                   EVAL       A=1

     C                   IF         B<=0
     C                   EVAL       B=1

     C                   EXFMT      FMT01

     C     A             DOWLT      10
     C                   EVAL       A=A+1
     C                   EVAL       B=B+1
     C                   ENDDO

     C                   ENDIF

     C                   ENDIF

     C                   EXFMT      FMT01

     C                   EVAL       MSG='A:'+%CHAR(A)
     C     MSG           DSPLY
     C                   EVAL       MSG='B:'+%CHAR(B)
     C     MSG           DSPLY