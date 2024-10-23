     C                   EXSR      SR
     C     1             IFEQ      0
     C     TAG_1         TAG
     C     '3'           DSPLY
     C                   ENDIF
     C                   SETON                                          LR

     C     SR            BEGSR
     C     1             IFEQ      1
     C     '1'           DSPLY
     C                   GOTO      TAG_1
     C     '2'           DSPLY
     C                   ENDIF
     C                   ENDSR
