     D RES             S             10    VARYING



      *
      * COMPARISON WITH NUMERIC
      *

     D LEFT            S              1  0
     D RIGHT           S              1  0

      * When LEFT and RIGHT are equal, with LEFT as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=1
     C     LEFT          DOWGE     1
     C                   EVAL      LEFT=0
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY

      * When RIGHT is greater than LEFT, with LEFT as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=1
     C     LEFT          DOWGE     2
     C                   EVAL      LEFT=2
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 0
     C     RES           DSPLY

      * When LEFT and RIGHT are equal, with RIGHT as variable
     C                   EVAL      RES=0
     C                   EVAL      RIGHT=1
     C     1             DOWGE     RIGHT
     C                   EVAL      RIGHT=2
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY

      * When RIGHT is greater than LEFT, with RIGHT as variable
     C                   EVAL      RES=0
     C                   EVAL      RIGHT=3
     C     2             DOWGE     RIGHT
     C                   EVAL      RIGHT=2
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 0
     C     RES           DSPLY

      * When LEFT and RIGHT are equal, both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=1
     C                   EVAL      RIGHT=1
     C     LEFT          DOWGE     RIGHT
     C                   EVAL      RIGHT=2
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY

      * When RIGHT is greater than LEFT, both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=2
     C                   EVAL      RIGHT=3
     C     LEFT          DOWGE     RIGHT
     C                   EVAL      RIGHT=2
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 0
     C     RES           DSPLY



      *
      * COMPARISON WITH STRING
      *

     D LEFT_STR        S             110          VARYING
     D RIGHT_STR       S             110          VARYING


      * When LEFT and RIGHT are equal, both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT_STR='1'
     C                   EVAL      RIGHT_STR='1'
     C     LEFT_STR      DOWGE     RIGHT_STR
     C                   EVAL      RIGHT_STR='2'
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY

      * When RIGHT is greater than LEFT, both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT_STR='1'
     C                   EVAL      RIGHT_STR='2'
     C     LEFT_STR      DOWGE     RIGHT_STR
     C                   EVAL      RIGHT_STR='1'
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 0
     C     RES           DSPLY