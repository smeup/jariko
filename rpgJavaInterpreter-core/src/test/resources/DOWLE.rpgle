     D RES             S             10    VARYING



      *
      * COMPARISON WITH NUMERIC
      *

     D LEFT            S              1  0
     D RIGHT           S              1  0

      * When LEFT and RIGHT are equal with LEFT as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=1
     C     LEFT          DOWLE     1
     C                   EVAL      LEFT=2
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY

      * When LEFT isn't lower than RIGHT, with LEFT as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=1
     C     LEFT          DOWLE     0
     C                   EVAL      LEFT=1
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 0
     C     RES           DSPLY

      * When LEFT and RIGHT are equal with RIGHT as variable
     C                   EVAL      RES=0
     C                   EVAL      RIGHT=1
     C     1             DOWLE     RIGHT
     C                   EVAL      RIGHT=0
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY

      * When LEFT isn't lower RIGHT, with RIGHT as variable
     C                   EVAL      RES=0
     C                   EVAL      RIGHT=1
     C     2             DOWLE     RIGHT
     C                   EVAL      RIGHT=3
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 0
     C     RES           DSPLY

      * When LEFT and RIGHT are equal and both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=1
     C                   EVAL      RIGHT=1
     C     LEFT          DOWLE     RIGHT
     C                   EVAL      RIGHT=0
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY

      * When LEFT isn't lower than RIGHT, both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=2
     C                   EVAL      RIGHT=1
     C     LEFT          DOWLE     RIGHT
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


      * When LEFT and RIGHT are equal and both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT_STR='1'
     C                   EVAL      RIGHT_STR='1'
     C     LEFT_STR      DOWLE     RIGHT_STR
     C                   EVAL      RIGHT_STR='0'
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY

      * When LEFT isn't lower than RIGHT, both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT_STR='1'
     C                   EVAL      RIGHT_STR='0'
     C     LEFT_STR      DOWLE     RIGHT_STR
     C                   EVAL      RIGHT_STR='1'
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 0
     C     RES           DSPLY