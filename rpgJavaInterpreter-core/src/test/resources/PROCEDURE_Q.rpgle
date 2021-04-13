      *---------------------------------------------------------------
     D PROCEDURE_01    PR
     D P1                             5  2
     D P2                             5  2 CONST
      *
     D PAR1_1          S              5  2
     D PAR1_2          S              5  2
     D RET_CHAR        S             10
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
      * Init vars
     C                   EVAL      PAR1_1=1,01
     C                   EVAL      PAR1_2=1,02
      *
     C                   CALLP     PROCEDURE_01(PAR1_1:PAR1_2)
      * Must be '5.10'
     C                   EVAL      RET_CHAR=%CHAR(PAR1_1)
     C     RET_CHAR      DSPLY
      *
      * Must be origin value of '1.02' instead of '10.20' due to 'CONST' keyword
     C                   EVAL      RET_CHAR=%CHAR(PAR1_2)
     C     RET_CHAR      DSPLY
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
     P PROCEDURE_01    B
     D PROCEDURE_01    PI
     D P1                             5  2
     D P2                             5  2 CONST
      *
     C                   EVAL      P1=P2*5
     C                   EVAL      P2=P1*2
      *
     P PROCEDURE_01    E
      *---------------------------------------------------------------