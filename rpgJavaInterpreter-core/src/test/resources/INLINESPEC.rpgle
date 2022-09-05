      * Evaluate the correctness of data reference resolution for statements that support inline dspec definition
      * Test IF
     C                   IF        1 = 1
      ** Test PARAM in branch THEN
     C                   CALL      'MYPGMB'
     C                   PARM      *BLANKS       MYPGMB           10
     C     MYPGMB        DSPLY
      ** Test CAT in branch THEN
     C     'A'           CAT       'A'           CATB             10
     C     CATB          DSPLY
      ** Test PARAM in branch ELSE
     C                   ELSE
     C                   CALL      'MYPGMC'
     C                   PARM      *BLANKS       MYPGMC           10
     C     MYPGMC        DSPLY
      ** Test CAT in branch ELSE
     C     'A'           CAT       'A'           CATC             10
     C     CATC          DSPLY
     C                   ENDIF
      * Test SELECT
     C                   SELECT
     C                   WHEN     1 = 1
      ** Test PARAM in branch WHEN
     C                   CALL      'MYPGMD'
     C                   PARM      *BLANKS       MYPGMD           10
     C     MYPGMD        DSPLY
      ** Test CAT in branch WHEN
     C     'A'           CAT       'A'           CATD             10
     C     CATD          DSPLY
     C                   WHEN     1 = 2
      ** Test PARAM in second branch WHEN
     C                   CALL      'MYPGME'
     C                   PARM      *BLANKS       MYPGME           10
      ** Test CAT in second branch WHEN
     C     'A'           CAT       'A'           CATE             10
     C     CATE          DSPLY
     C     MYPGME        DSPLY
      *
     C                   ENDSL