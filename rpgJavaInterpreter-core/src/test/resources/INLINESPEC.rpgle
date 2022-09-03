
     C                   IF        1 = 1
     C                   CALL      'MYPGMB'
     C                   PARM      *BLANKS       MYPGMB           10
     C     MYPGMB        DSPLY
     C                   ELSE
     C                   CALL      'MYPGMC'
     C                   PARM      *BLANKS       MYPGMC           10
     C     MYPGMC        DSPLY
     C                   ENDIF
     C                   SELECT
     C                   WHEN     1 = 1
     C                   CALL      'MYPGMD'
     C                   PARM      *BLANKS       MYPGMD           10
     C     MYPGMD        DSPLY
     C                   WHEN     1 = 2
     C                   CALL      'MYPGME'
     C                   PARM      *BLANKS       MYPGME           10
     C     MYPGME        DSPLY
     C                   ENDSL