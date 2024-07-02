      * Test DOWxx behavior

     FSM03V     CF   E             WORKSTN USROPN
     D MSG             S             50          VARYING
     D A               S              2  0
     D B               S              2  0
     D I               S              2  0

     C     I             DOWEQ     0

     C                   IF        A<=0
     C                   EXFMT     FMT01
     C                   ENDIF

     C                   EVAL      I='A:'+%CHAR(A)
     C                   ENDDO


     C                   EVAL      MSG='A:'+%CHAR(A)
     C     MSG           DSPLY
     C                   EVAL      MSG='B:'+%CHAR(B)
     C     MSG           DSPLY