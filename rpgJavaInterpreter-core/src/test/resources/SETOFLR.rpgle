     C     'Before'      DSPLY
     C                   EXSR      PRINTFLG
     **********************************************************************
     C                   SETOFF                                       LR
     C     'After set'   DSPLY
     C                   EXSR      PRINTFLG
     **********************************************************************
     **********************************************************************
     C                   SETON                                          LR
     **********************************************************************
     C     PRINTFLG      BEGSR
     C                   if        *inlr
     C     'LR On'       dsply
     C                   ELSE
     C     'LR Off'      dsply
     C                   ENDIF
     C                   ENDSR
