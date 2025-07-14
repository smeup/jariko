     V* ==============================================================
     V* 14/07/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program executes `ADD` operator from an array to a
    O *  standalone the procedure `PR2` which calls `PR1`.
    O * In this case the program mustn't compile.
     V* ==============================================================
     DVAL              S              5  2 INZ(5)
     DARR              S              5  2 DIM(3) INZ(2)
     DMSG              S              5

     C                   EXSR      SHOW_RES
     C                   ADD       ARR           VAL
     C                   EXSR      SHOW_RES
     C                   SETON                                          LR

     C     SHOW_RES      BEGSR
     C                   EVAL      MSG=%CHAR(VAL)
     C     MSG           DSPLY
     C                   ENDSR