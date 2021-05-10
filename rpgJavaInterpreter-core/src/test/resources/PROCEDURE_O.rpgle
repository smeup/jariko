      *---------------------------------------------------------------
      * Tested features:
      * RIGHT HANDLING OF '*NOPASS' ATTRIBUTE OF 'OPTION' KEYWORD
      * ('*NOPASS' means parameter is not mandatory)
      *---------------------------------------------------------------
     D PROCEDURE_01    PR
     D P1                             5  2
     D P2                             5  2
     D P3                             5  2
     D P4                             5  2 OPTIONS(*NOPASS)
     D P5                             5  2 OPTIONS(*NOPASS)
      *---------------------------------------------------------------
     D PROCEDURE_02    PR
     D P1                             5  2
     D P2                             5  2
     D P3                             5  2
     D P4                             5  2 OPTIONS(*NOPASS)
     D P5                             5  2 OPTIONS(*NOPASS)
      *---------------------------------------------------------------
     D PROCEDURE_03    PR            10  2
     D P1                             5  2
     D P2                             5  2
     D P3                             5  2
     D P4                             5  2 OPTIONS(*NOPASS)
     D P5                             5  2 OPTIONS(*NOPASS)
      *---------------------------------------------------------------
     D PROCEDURE_04    PR            10  2
     D P1                             5  2
     D P2                             5  2
     D P3                             5  2
     D P4                             5  2 OPTIONS(*NOPASS)
     D P5                             5  2 OPTIONS(*NOPASS)
      *---------------------------------------------------------------
     D P1_1            S              5  2
     D P1_2            S              5  2
     D P1_3            S              5  2
     D P1_4            S              5  2
     D P1_5            S              5  2
      *---------------------------------------------------------------
     D R1_1            S             10  2
     D R1_2            S             10  2
     D R1_3            S             10  2
     D R1_4            S             10  2
     D R1_5            S             10  2
     D RET_CHAR        S             10
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
      * Execute procedures by 'CALLP' statement
     C                   EXSR      PROC_CALLP
      *
      * Execute procedures by 'EVAL' statement
     C                   EXSR      PROC_EVAL
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
      * Execute procedures by 'CALLP'
      *---------------------------------------------------------------
     C     PROC_CALLP    BEGSR
      * Init vars
     C                   EVAL      P1_1=1,01
     C                   EVAL      P1_2=1,02
     C                   EVAL      P1_3=1,03
     C                   EVAL      P1_4=1,04
     C                   EVAL      P1_5=1,05
      * Due to '*NOPASS' option, can pass only 3 mandatory parameters
     C                   CALLP     PROCEDURE_01(P1_1:P1_2:P1_3)
      *
      * Must be '1.01'
     C                   EVAL      RET_CHAR=%CHAR(P1_1)
     C     RET_CHAR      DSPLY
      *
      * Must be '2.04'
     C                   EVAL      RET_CHAR=%CHAR(P1_2)
     C     RET_CHAR      DSPLY
      *
      * Must be '3.09'
     C                   EVAL      RET_CHAR=%CHAR(P1_3)
     C     RET_CHAR      DSPLY
      *
      * Must be '1.04'
     C                   EVAL      RET_CHAR=%CHAR(P1_4)
     C     RET_CHAR      DSPLY
      *
      * Must be '1.05'
     C                   EVAL      RET_CHAR=%CHAR(P1_5)
     C     RET_CHAR      DSPLY
      *
      * Init vars
     C                   EVAL      P1_1=2,21
     C                   EVAL      P1_2=2,22
     C                   EVAL      P1_3=2,23
     C                   EVAL      P1_4=2,24
     C                   EVAL      P1_5=2,25
      *
     C                   CALLP     PROCEDURE_02(P1_1:P1_2:P1_3:P1_4:P1_5)
      * Must be '2.22'
     C                   EVAL      RET_CHAR=%CHAR(P1_1)
     C     RET_CHAR      DSPLY
      *
      * Must be '2.24'
     C                   EVAL      RET_CHAR=%CHAR(P1_2)
     C     RET_CHAR      DSPLY
      *
      * Must be '2.26'
     C                   EVAL      RET_CHAR=%CHAR(P1_3)
     C     RET_CHAR      DSPLY
      *
      * Must be '2.28'
     C                   EVAL      RET_CHAR=%CHAR(P1_4)
     C     RET_CHAR      DSPLY
      *
      * Must be '2.30'
     C                   EVAL      RET_CHAR=%CHAR(P1_5)
     C     RET_CHAR      DSPLY
      *
     C                   ENDSR
      *---------------------------------------------------------------
      * Execute procedures by 'EVAL'
      *---------------------------------------------------------------
     C     PROC_EVAL     BEGSR
      * Init vars
     C                   EVAL      P1_1=1,01
     C                   EVAL      P1_2=1,02
     C                   EVAL      P1_3=1,03
     C                   EVAL      P1_4=1,04
     C                   EVAL      P1_5=1,05
      * Due to '*NOPASS' option, can pass only 3 mandatory parameters
     C                   EVAL      R1_1=PROCEDURE_03(P1_1:P1_2:P1_3)
      * Must be '6.14'
     C                   EVAL      RET_CHAR=%CHAR(R1_1)
     C     RET_CHAR      DSPLY
      *
      * Must be '1.01'
     C                   EVAL      RET_CHAR=%CHAR(P1_1)
     C     RET_CHAR      DSPLY
      *
      * Must be '2.04'
     C                   EVAL      RET_CHAR=%CHAR(P1_2)
     C     RET_CHAR      DSPLY
      *
      * Must be '3.09'
     C                   EVAL      RET_CHAR=%CHAR(P1_3)
     C     RET_CHAR      DSPLY
      *
      * Must be '1.04'
     C                   EVAL      RET_CHAR=%CHAR(P1_4)
     C     RET_CHAR      DSPLY
      *
      * Must be '1.05'
     C                   EVAL      RET_CHAR=%CHAR(P1_5)
     C     RET_CHAR      DSPLY
      *
      * Init vars
     C                   EVAL      P1_1=2,21
     C                   EVAL      P1_2=2,22
     C                   EVAL      P1_3=2,23
     C                   EVAL      P1_4=2,24
     C                   EVAL      P1_5=2,25
      *
     C                   EVAL      R1_1=
     C                             PROCEDURE_04(P1_1:P1_2:P1_3:P1_4:P1_5)
      * Must be '11.3'
     C                   EVAL      RET_CHAR=%CHAR(R1_1)
     C     RET_CHAR      DSPLY
      *
      * Must be '2.22'
     C                   EVAL      RET_CHAR=%CHAR(P1_1)
     C     RET_CHAR      DSPLY
      *
      * Must be '2.24'
     C                   EVAL      RET_CHAR=%CHAR(P1_2)
     C     RET_CHAR      DSPLY
      *
      * Must be '2.26'
     C                   EVAL      RET_CHAR=%CHAR(P1_3)
     C     RET_CHAR      DSPLY
      *
      * Must be '2.28'
     C                   EVAL      RET_CHAR=%CHAR(P1_4)
     C     RET_CHAR      DSPLY
      *
      * Must be '2.30'
     C                   EVAL      RET_CHAR=%CHAR(P1_5)
     C     RET_CHAR      DSPLY
      *
     C                   ENDSR
      *---------------------------------------------------------------
     P PROCEDURE_01    B
     D PROCEDURE_01    PI
     D P1                             5  2
     D P2                             5  2
     D P3                             5  2
     D P4                             5  2 OPTIONS(*NOPASS)
     D P5                             5  2 OPTIONS(*NOPASS)
      *
     C                   EVAL      P1=P1*1
     C                   EVAL      P2=P2*2
     C                   EVAL      P3=P3*3
      *
     P PROCEDURE_01    E
      *---------------------------------------------------------------
     P PROCEDURE_02    B
     D PROCEDURE_02    PI
     D P1                             5  2
     D P2                             5  2
     D P3                             5  2
     D P4                             5  2 OPTIONS(*NOPASS)
     D P5                             5  2 OPTIONS(*NOPASS)
      *
     C                   EVAL      P1=P1+0.01
     C                   EVAL      P2=P2+0.02
     C                   EVAL      P3=P3+0.03
     C                   EVAL      P4=P4+0.04
     C                   EVAL      P5=P5+0.05
      *
     P PROCEDURE_02    E
      *---------------------------------------------------------------
     P PROCEDURE_03    B
     D PROCEDURE_03    PI            10  2
     D P1                             5  2
     D P2                             5  2
     D P3                             5  2
     D P4                             5  2 OPTIONS(*NOPASS)
     D P5                             5  2 OPTIONS(*NOPASS)
      *
     C                   EVAL      P1=P1*1
     C                   EVAL      P2=P2*2
     C                   EVAL      P3=P3*3
     C                   RETURN    P1+P2+P3
      *
     P PROCEDURE_03    E
      *---------------------------------------------------------------
     P PROCEDURE_04    B
     D PROCEDURE_04    PI            10  2
     D P1                             5  2
     D P2                             5  2
     D P3                             5  2
     D P4                             5  2 OPTIONS(*NOPASS)
     D P5                             5  2 OPTIONS(*NOPASS)
      *
     C                   EVAL      P1=P1+0.01
     C                   EVAL      P2=P2+0.02
     C                   EVAL      P3=P3+0.03
     C                   EVAL      P4=P4+0.04
     C                   EVAL      P5=P5+0.05
     C                   RETURN    P1+P2+P3+P4+P5
      *
     P PROCEDURE_04    E
      *---------------------------------------------------------------