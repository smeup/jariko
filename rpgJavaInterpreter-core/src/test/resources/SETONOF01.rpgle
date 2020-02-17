     C     'Before'      DSPLY
     C                   EXSR      PRINTFLG
     **********************************************************************
     C                   SETON                                        56
     C     'After set'   DSPLY
     C                   EXSR      PRINTFLG
     **********************************************************************
     C                   SETOFF                                       56
     C                   SETOFF                                       57
     C     'After off'   DSPLY
     C                   EXSR      PRINTFLG
     **********************************************************************
     C                   SETON                                          LR
     **********************************************************************
     C     PRINTFLG      BEGSR
     C                   if        (not *in56) and (not *in57)
     C     '56=off57=off'dsply
     C                   ENDIF
     C                   if        *in56
     C     '56=on'       dsply
     C                   ENDIF
     C                   if        *in57
     C     '57=on'       dsply
     C                   ENDIF
     C                   ENDSR
