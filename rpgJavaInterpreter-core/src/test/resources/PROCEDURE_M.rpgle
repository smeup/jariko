      *---------------------------------------------------------------
      * Tested features:
      * PREVENT CONFLICTS IN TWO PROCEDURE WITH SAME PARAMETER NAMES
      *---------------------------------------------------------------
     D PROCEDURE_01    PR
     D P1                             5  2
     D R1                            10  2
      *
     D PAR1            S              5  2
      *---------------------------------------------------------------
     D PROCEDURE_02    PR            10  2
     D P1                             7  2
      *
     D PAR2            S              7  2
      *---------------------------------------------------------------
     D RET             S             10  2
     D RET_CHAR        S             10
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
      *
     C                   EVAL      RET=0
     C                   EVAL      PAR1=1,12
     C                   CALLP     PROCEDURE_01(PAR1:RET)
      * Must be '2.24'
     C                   EVAL      RET_CHAR=%CHAR(RET)
     C     RET_CHAR      DSPLY
      *
     C                   EVAL      RET=0
     C                   EVAL      PAR2=1,12
     C                   EVAL      RET=PROCEDURE_02(PAR2)
      * Must be '3.36'
     C                   EVAL      RET_CHAR=%CHAR(RET)
     C     RET_CHAR      DSPLY
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
     P PROCEDURE_01    B
     D PROCEDURE_01    PI
     D P1                             5  2
     D R1                            10  2
      *
     C                   EVAL      R1=P1*2
      *
     P PROCEDURE_01    E
      *---------------------------------------------------------------
     P PROCEDURE_02    B
     D PROCEDURE_02    PI            10  2
     D P1                             7  2
      *
     C                   RETURN    P1*3
      *
     P PROCEDURE_02    E
      *---------------------------------------------------------------