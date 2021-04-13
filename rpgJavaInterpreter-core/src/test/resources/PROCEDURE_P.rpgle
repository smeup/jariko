      *---------------------------------------------------------------
     D PROCEDURE_01    PR
     D P1                             5  2
     D P2              C                   CONST(8.9)
     D P3                             5  2 VALUE OPTIONS(*NOPASS)
     D P4                             5  2 OPTIONS(*NOPASS)
     D P5                             5  2 OPTIONS(*NOPASS)
      *
     D PAR1_1          S              5  2
     D PAR1_2          S              5  2
     D PAR1_3          S              5  2
     D PAR1_4          S              5  2
     D PAR1_5          S              5  2
     D RET_CHAR        S             10
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
      * Init vars
     C                   EVAL      PAR1_1=1,01
     C                   EVAL      PAR1_2=1,02
     C                   EVAL      PAR1_3=1,03
     C                   EVAL      PAR1_4=1,04
     C                   EVAL      PAR1_5=1,05
      * Due to '*NOPASS' option, can pass only 3 mandatory parameters
     C                   CALLP     PROCEDURE_01(PAR1_1:PAR1_2:PAR1_3)
      *
      * Must be '17.8'
     C                   EVAL      RET_CHAR=%CHAR(PAR1_1)
     C     RET_CHAR      DSPLY
      *
      * Must be '1.02'
     C                   EVAL      RET_CHAR=%CHAR(PAR1_2)
     C     RET_CHAR      DSPLY
      *
      * Must be '1.03' instead of '3.09' due to 'VALUE' keyword
     C                   EVAL      RET_CHAR=%CHAR(PAR1_3)
     C     RET_CHAR      DSPLY
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
     P PROCEDURE_01    B
     D PROCEDURE_01    PI
     D P1                             5  2
     D P2              C                   CONST(8.9)
     D P3                             5  2 VALUE OPTIONS(*NOPASS)
     D P4                             5  2 OPTIONS(*NOPASS)
     D P5                             5  2 OPTIONS(*NOPASS)
      *
     C                   EVAL      P1=P2*2
     C                   EVAL      P3=P3*3
      *
     P PROCEDURE_01    E
      *---------------------------------------------------------------