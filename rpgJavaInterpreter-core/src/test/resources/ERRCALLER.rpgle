     D Msg             S             50
     D N               S              8  0
     *********************************************************************
     C                   EVAL      N = 1
     C                   CALL      'ERRCALLED'                          69
     C                   PARM                    n
     C                   EXSR      SHOWERROR
     *********************************************************************
     C                   EVAL      N = 0
     C                   CALL      'ERRCALLED'                          69
     C                   PARM                    n
     C                   EXSR      SHOWERROR
     *********************************************************************
     C                   SETON                                        LR
     *********************************************************************
     C     SHOWERROR     BEGSR
     C                   IF        *IN69
     C     'Got Error'   DSPLY
     C                   ELSE
     C     'No Error'    DSPLY
     C                   ENDIF
     C                   SETOFF                                         69
     C                   ENDSR
