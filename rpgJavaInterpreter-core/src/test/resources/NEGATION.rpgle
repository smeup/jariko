      *
      * NEGATION OF NUMBERS
      *

     D NUM             S              3  0
     D NUMD            S              5  2
     D NUM_NEG         S              3  0
     D NUMD_NEG        S              3  2
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

     C                   EVAL      NUM=-10
     C                   EVAL      RES=%CHAR(NUM)
     C* Excepted -10
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

     C                   EVAL      NUM= -10
     C                   EVAL      RES=%CHAR(NUM)
     C* Excepted -10
     C     RES           DSPLY



     C                   EVAL      NUMD=1.50
     C                   EVAL      NUMD_NEG=-NUMD
     C                   EVAL      RES=%CHAR(NUMD_NEG)
     C* Excepted -1.50
     C     RES           DSPLY

     C                   EVAL      NUMD=0
     C                   EVAL      NUMD=-NUMD_NEG
     C                   EVAL      RES=%CHAR(NUMD)
     C* Excepted 1.50
     C     RES           DSPLY

     C                   EVAL      NUMD= -1.50
     C                   EVAL      RES=%CHAR(NUMD)
     C* Excepted -1.50
     C     RES           DSPLY



     C                   EVAL      NUMD=1.50
     C                   EVAL      NUMD_NEG= -NUMD
     C                   EVAL      RES=%CHAR(NUMD_NEG)
     C* Excepted -1.50
     C     RES           DSPLY

     C                   EVAL      NUMD=0
     C                   EVAL      NUMD= -NUMD_NEG
     C                   EVAL      RES=%CHAR(NUMD)
     C* Excepted 1.50

     C     RES           DSPLY
     C                   EVAL      NUMD= -1.50
     C                   EVAL      RES=%CHAR(NUMD)
     C* Excepted -1.50
     C     RES           DSPLY