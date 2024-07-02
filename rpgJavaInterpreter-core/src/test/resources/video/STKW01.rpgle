     V* ==============================================================
     D* 02/07/24
     D* Test the stack trace write behavior by placing EXFMT in certain
     D* points to allow test class to read it
     V* ==============================================================

     FSTKW01V   CF   E             WORKSTN USROPN
     D MSG             S             50          VARYING
     D A               S              2  0
     D B               S              2  0

      * --- STACK check --- *
     C                   EXFMT     FMT01

     C                   IF        A<=0
     C                   EVAL      A=1

      * --- STACK check --- *
     C                   EXFMT     FMT01

     C                   IF        B<=0
     C                   EVAL      B=1

      * --- STACK check --- *
     C                   EXFMT     FMT01

     C                   ENDIF

     C                   ENDIF

      * --- STACK check --- *
     C                   EXFMT     FMT01

     C                   EVAL      MSG='A:'+%CHAR(A)
     C     MSG           DSPLY
     C                   EVAL      MSG='B:'+%CHAR(B)
     C     MSG           DSPLY