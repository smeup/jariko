      * Evaluate the correctness of data reference resolution for statements that support inline dspec definition
      * Test in body
      ** CALL
     C                   CALL      'MYPGMA'
     C                   PARM      *BLANKS       MYPGMA           10
     C     MYPGMA        DSPLY
      ** CAT
     C     'A'           CAT       'A'           CATA             10
     C     CATA          DSPLY
      ** MOVE
     C     'A'           MOVE      'A'           MOVEA            10
     C     MOVEA         DSPLY
      *
      ** MOVEA
     C     'A'           MOVEA     'A'           MOVEAA           10
     C     MOVEAA        DSPLY
      *
      * Test IF
     C                   IF        1 = 1
      ** Test in branch THEN
     C                   CALL      'MYPGMB'
     C                   PARM      *BLANKS       MYPGMB           10
     C     MYPGMB        DSPLY
      ** Test in branch ELSE
     C                   ELSE
     C                   CALL      'MYPGMC'
     C                   PARM      *BLANKS       MYPGMC           10
     C     MYPGMC        DSPLY
     C                   ENDIF
      *
      * Test SELECT
     C                   SELECT
     C                   WHEN     1 = 1
      ** Test in first branch WHEN
     C                   CALL      'MYPGMD'
     C                   PARM      *BLANKS       MYPGMD           10
     C     MYPGMD        DSPLY
     C                   ENDSL