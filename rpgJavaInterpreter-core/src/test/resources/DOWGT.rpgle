     D RES             S             10    VARYING



      *
      * COMPARISON WITH NUMERIC
      *

     D LEFT            S              1  0
     D RIGHT           S              1  0

      * When LEFT is greater than RIGHT, with LEFT as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=2
     C     LEFT          DOWGT     1
     C                   EVAL      LEFT=1
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY

      * When LEFT isn't greater than RIGHT, with LEFT as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=1
     C     LEFT          DOWGT     2
     C                   EVAL      LEFT=2
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 0
     C     RES           DSPLY

      * When LEFT is greater than RIGHT, with RIGHT as variable
     C                   EVAL      RES=0
     C                   EVAL      RIGHT=0
     C     1             DOWGT     RIGHT
     C                   EVAL      RIGHT=1
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY

      * When LEFT isn't greater than RIGHT, with RIGHT as variable
     C                   EVAL      RES=0
     C                   EVAL      RIGHT=2
     C     2             DOWGT     RIGHT
     C                   EVAL      RIGHT=1
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 0
     C     RES           DSPLY

      * When LEFT is greater than RIGHT, both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=2
     C                   EVAL      RIGHT=1
     C     LEFT          DOWGT     RIGHT
     C                   EVAL      RIGHT=2
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY

      * When LEFT and RIGHT are equal and both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=2
     C                   EVAL      RIGHT=2
     C     LEFT          DOWGT     RIGHT
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


      * When LEFT is greater than RIGHT, both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT_STR='2'
     C                   EVAL      RIGHT_STR='1'
     C     LEFT_STR      DOWGT     RIGHT_STR
     C                   EVAL      RIGHT_STR='2'
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY

      * When LEFT and RIGHT aren't equal and both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT_STR='2'
     C                   EVAL      RIGHT_STR='2'
     C     LEFT_STR      DOWGT     RIGHT_STR
     C                   EVAL      RIGHT_STR='3'
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 0
     C     RES           DSPLY