     D Msg             S             50
     D N               S              8  0
     *********************************************************************
     C                   EVAL      N = 1
     C                   CALL      'RTCALLED'                             69
     C                   PARM                    n
     C                   EXSR      SHOWRESULT
     *********************************************************************
     C                   EVAL      N = 0
     C                   CALL      'RTCALLED'                             69
     C                   PARM                    n
     C                   EXSR      SHOWRESULT
     *********************************************************************
     C                   SETON                                        LR
     *********************************************************************
     C     SHOWRESULT    BEGSR
     C                   IF        *IN69
     C     'End LR'      DSPLY
     C                   ELSE
     C     'End RT'      DSPLY
     C                   ENDIF
     C                   SETOFF                                         69
     C                   ENDSR
