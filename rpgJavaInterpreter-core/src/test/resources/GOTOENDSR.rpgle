     C                   EXSR      SR1
     **********************************************************************
     C                   SETON                                          LR
     **********************************************************************
     C     SR1           BEGSR
     C                   CLEAR                   N                 1 0
     C     START         TAG
     C                   IF        N >= 3
     C                   GOTO      endsr1
     C                   ENDIF
     C                   ADD       1             N
     C     N             DSPLY
     C                   goto      start
     C     ENDSR1        ENDSR
