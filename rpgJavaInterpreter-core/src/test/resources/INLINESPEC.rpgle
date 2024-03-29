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
      ** SCAN
     C     'A'           SCAN      'A'           SCANA            1  0
     C     SCANA         DSPLY
      *
      ** MULT
     C     1             MULT      1             MULTA            1  0
     C     MULTA         DSPLY
      *
      ** DIV
     C     1             DIV       1             DIVA             1  0
     C     DIVA          DSPLY
      *
      ** DO
     C     1             DO        2             DOA              1  0
     C     DOA           DSPLY
     C                   ENDDO
      *
      ** SELECT
     C                   SELECT                  SELA             1  0
     C                   WHEN     1 = 1
     C     SELA          DSPLY
     C                   ENDSL
      *
      *
      *
      * Test in-line DSPEC in IF
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
      * Test in-line DSPEC in SELECT
     C                   SELECT
     C                   WHEN     1 = 1
      ** Test in first branch WHEN
     C                   CALL      'MYPGMD'
     C                   PARM      *BLANKS       MYPGMD           10
     C     MYPGMD        DSPLY
     C                   ENDSL