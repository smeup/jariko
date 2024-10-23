     C                   EXSR      SR
     C     TAG_1         TAG
     C     '3'           DSPLY
     C                   SETON                                          LR

     C     SR            BEGSR
     C                   GOTO      TAG_1
     C     1             IFEQ      1
     C     '1'           DSPLY
     C     '2'           DSPLY
     C                   ENDIF
     C                   ENDSR
