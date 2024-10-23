     C                   EXSR      SR1
     C     1             IFEQ      0
     C     TAG_1         TAG
     C     '3'           DSPLY
     C                   ENDIF
     C                   SETON                                          LR

     C     SR1           BEGSR
     C     1             IFEQ      1
     C     '1'           DSPLY
     C                   EXSR      SR2
     C     '2'           DSPLY
     C                   ENDIF
     C                   ENDSR

     C     SR2           BEGSR
     C     1             IFEQ      1
     C     '4'           DSPLY
     C                   GOTO      TAG_1
     C     '5'           DSPLY
     C                   ENDIF
     C                   ENDSR
