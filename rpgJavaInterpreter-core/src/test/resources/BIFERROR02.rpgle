     V* ==============================================================
     D* %ERROR with CALL(E) when the called program fails inside
     V* ==============================================================
     D MSG             S             20
     D N               S              8  0
     C                   EVAL      N = 1
     C                   CALL(E)   'ERRCALLED'
     C                   PARM                    N
     C                   IF        %ERROR
     C                   EVAL      MSG = 'CALL N=1 ERR=Y'
     C                   ELSE
     C                   EVAL      MSG = 'CALL N=1 ERR=N'
     C                   ENDIF
     C     MSG           DSPLY
     C                   EVAL      N = 0
     C                   CALL(E)   'ERRCALLED'
     C                   PARM                    N
     C                   IF        %ERROR
     C                   EVAL      MSG = 'CALL N=0 ERR=Y'
     C                   ELSE
     C                   EVAL      MSG = 'CALL N=0 ERR=N'
     C                   ENDIF
     C     MSG           DSPLY
     C                   SETON                                        LR
