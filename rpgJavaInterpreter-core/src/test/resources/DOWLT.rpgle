     D RES             S             10    VARYING



      *
      * COMPARISON WITH NUMERIC
      *

     D LEFT            S              1  0
     D RIGHT           S              1  0

      * When LEFT isn't lower than RIGHT, with LEFT as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=2
     C     LEFT          DOWLT     1
     C                   EVAL      LEFT=1
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 0
     C     RES           DSPLY

      * When LEFT is lower than RIGHT, with LEFT as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=1
     C     LEFT          DOWLT     2
     C                   EVAL      LEFT=2
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY

      * When LEFT isn't lower than RIGHT, with RIGHT as variable
     C                   EVAL      RES=0
     C                   EVAL      RIGHT=0
     C     1             DOWLT     RIGHT
     C                   EVAL      RIGHT=1
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 0
     C     RES           DSPLY

      * When LEFT is lower than RIGHT, with RIGHT as variable
     C                   EVAL      RES=0
     C                   EVAL      RIGHT=3
     C     2             DOWLT     RIGHT
     C                   EVAL      RIGHT=2
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY

      * When LEFT isn't lower than RIGHT, both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=2
     C                   EVAL      RIGHT=1
     C     LEFT          DOWLT     RIGHT
     C                   EVAL      RIGHT=2
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 0
     C     RES           DSPLY

      * When LEFT is lower than RIGHT, both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT=2
     C                   EVAL      RIGHT=3
     C     LEFT          DOWLT     RIGHT
     C                   EVAL      RIGHT=2
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY



      *
      * COMPARISON WITH STRING
      *

     D LEFT_STR        S             110          VARYING
     D RIGHT_STR       S             110          VARYING


      * When LEFT isn't lower than RIGHT, both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT_STR='2'
     C                   EVAL      RIGHT_STR='1'
     C     LEFT_STR      DOWLT     RIGHT_STR
     C                   EVAL      RIGHT_STR='2'
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 0
     C     RES           DSPLY

      * When LEFT is lower than RIGHT, both as variable
     C                   EVAL      RES=0
     C                   EVAL      LEFT_STR='2'
     C                   EVAL      RIGHT_STR='3'
     C     LEFT_STR      DOWLT     RIGHT_STR
     C                   EVAL      RIGHT_STR='2'
     C                   EVAL      RES=1
     C                   ENDDO
      * Excepted 1
     C     RES           DSPLY