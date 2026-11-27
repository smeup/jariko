     V* ==============================================================
     D* %ERROR with the (E) extender on OPEN and CALL
     V* ==============================================================
     FPRT198    O    F  198        PRINTER OFLIND(*INOF) USROPN
     D MSG             S             20
     C                   OPEN(E)   PRT198
     C                   IF        %ERROR
     C                   EVAL      MSG = 'OPEN PRT=ERR'
     C                   ELSE
     C                   EVAL      MSG = 'OPEN PRT=OK'
     C                   ENDIF
     C     MSG           DSPLY
     C                   CALL(E)   'NOPROGXX'
     C                   IF        %ERROR
     C                   EVAL      MSG = 'CALL NOPROG=ERR'
     C                   ELSE
     C                   EVAL      MSG = 'CALL NOPROG=OK'
     C                   ENDIF
     C     MSG           DSPLY
     C                   SETON                                        LR
