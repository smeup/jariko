     C                   EXSR      SR
     C                   SETON                                          LR

     C     SR            BEGSR
     C     1             IFEQ      1
     C     '1'           DSPLY
     C                   GOTO      TAG_1
     C     '2'           DSPLY
     C                   ENDIF
     C     TAG_1         TAG
     C                   ENDSR
