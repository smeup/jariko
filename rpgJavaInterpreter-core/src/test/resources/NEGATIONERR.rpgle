      *
      * NEGATION OF STRING
      *

     D STR             S             100          VARYING INZ('10')
     D STR_NEG         S             100          VARYING
     D RES             S             100          VARYING

     C                   EVAL      STR_NEG=-STR
     C                   EVAL      RES=%CHAR(STR_NEG)
     C* Excepted Exception
     C     RES           DSPLY