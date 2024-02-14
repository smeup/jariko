      *
      * NEGATION OF NUMBER
      *

     D NUM             S              3  0
     D NUM_NEG         S              3  0
     D RES             S             100          VARYING

     C                   EVAL      NUM=10
     C                   EVAL      NUM_NEG=-NUM
     C                   EVAL      RES=%CHAR(NUM_NEG)
     C* Excepted -10
     C     RES           DSPLY

     C                   EVAL      NUM=0
     C                   EVAL      NUM=-NUM_NEG
     C                   EVAL      RES=%CHAR(NUM)
     C* Excepted 10
     C     RES           DSPLY


     C                   EVAL      NUM=10
     C                   EVAL      NUM_NEG= -NUM
     C                   EVAL      RES=%CHAR(NUM_NEG)
     C* Excepted -10
     C     RES           DSPLY

     C                   EVAL      NUM=0
     C                   EVAL      NUM= -NUM_NEG
     C                   EVAL      RES=%CHAR(NUM)
     C* Excepted 10
     C     RES           DSPLY