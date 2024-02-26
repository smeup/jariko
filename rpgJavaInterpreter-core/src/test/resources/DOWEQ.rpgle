     D RES             S             10    VARYING



      *
      * COMPARISON WITH NUMERIC
      *

     D LEFT            S              1  0
     D RIGHT           S              1  0

      * When LEFT and RIGHT are equal with LEFT as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=1
     C     LEFT          DOWEQ     1
     C                   EVAL      LEFT=2
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY

      * When LEFT and RIGHT aren't equal with LEFT as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=1
     C     LEFT          DOWEQ     2
     C                   EVAL      LEFT=2
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 0
     C     RES           DSPLY

      * When LEFT and RIGHT are equal with RIGHT as variable
     C                   EVAL      RES=0
     C                   EVAL      RIGHT=1
     C     1             DOWEQ     RIGHT
     C                   EVAL      RIGHT=2
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY

      * When LEFT and RIGHT aren't equal with RIGHT as variable
     C                   EVAL      RES=0
     C                   EVAL      RIGHT=1
     C     2             DOWEQ     RIGHT
     C                   EVAL      RIGHT=2
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 0
     C     RES           DSPLY

      * When LEFT and RIGHT are equal and both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=1
     C                   EVAL      RIGHT=1
     C     LEFT          DOWEQ     RIGHT
     C                   EVAL      RIGHT=2
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY

      * When LEFT and RIGHT aren't equal and both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=1
     C                   EVAL      RIGHT=2
     C     LEFT          DOWEQ     RIGHT
     C                   EVAL      RIGHT=3
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 0
     C     RES           DSPLY



      *
      * COMPARISON WITH STRING
      *

     D LEFT_STR        S             110          VARYING
     D RIGHT_STR       S             110          VARYING


      * When LEFT and RIGHT are equal and both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT_STR='1'
     C                   EVAL      RIGHT_STR='1'
     C     LEFT_STR      DOWEQ     RIGHT_STR
     C                   EVAL      RIGHT_STR='2'
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY
      * When LEFT and RIGHT aren't equal and both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT_STR='1'
     C                   EVAL      RIGHT_STR='2'
     C     LEFT_STR      DOWEQ     RIGHT_STR
     C                   EVAL      RIGHT_STR='3'
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 0
     C     RES           DSPLY