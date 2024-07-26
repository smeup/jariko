     D RESULT          S           2560    VARYING
     D FOO             S              1

     C                   SETON                                            36
     C                   EVAL      FOO = ''
     C                   MOVEL     FOO           *IN36
     C                   EVAL      RESULT = %CHAR(*IN36)
     C     RESULT        DSPLY
