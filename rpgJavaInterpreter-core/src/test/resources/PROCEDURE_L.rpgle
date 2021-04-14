      *---------------------------------------------------------------
     D PROCEDURE_01    PR
     D P1                             5  2
     D R1                            10  2 VALUE
      *
     D PAR1            S              5  2
      *---------------------------------------------------------------
     D PROCEDURE_02    PR            10  2
     D P1                             7  2 VALUE
      *
     D PAR2            S              7  2
      *---------------------------------------------------------------
     D RET             S             10  2
     D RET_CHAR        S             10
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
      *
     C                   EVAL      PAR1=1,12
     C                   EVAL      RET=0,99
     C                   CALLP     PROCEDURE_01(PAR1:RET)
      * Must be '.99'
     C                   EVAL      RET_CHAR=%CHAR(RET)
     C     RET_CHAR      DSPLY
      *
     C                   EVAL      RET=0
     C                   EVAL      PAR2=1,11
     C                   EVAL      RET=PROCEDURE_02(PAR2)
      * PAR2 Must be '1.11'
     C                   EVAL      RET_CHAR=%CHAR(PAR2)
     C     RET_CHAR      DSPLY
      * RET Must be '9.99'
     C                   EVAL      RET_CHAR=%CHAR(RET)
     C     RET_CHAR      DSPLY
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
     P PROCEDURE_01    B
     D PROCEDURE_01    PI
     D P1                             5  2
     D R1                            10  2 VALUE
      *
     C                   EVAL      R1=P1*2
      *
     P PROCEDURE_01    E
      *---------------------------------------------------------------
     P PROCEDURE_02    B
     D PROCEDURE_02    PI            10  2
     D P1                             7  2 VALUE
      *
     C                   EVAL      P1=P1*3
     C                   RETURN    P1*3
      *
     P PROCEDURE_02    E
      *---------------------------------------------------------------