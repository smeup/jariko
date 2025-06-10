     V* ==============================================================
     V* 06/06/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Calling a program by passing Decimal to a DS which defines
    O *  only one field as Integer.
     V* ==============================================================
     D B£2$DS          DS           100
     D  ££UMHR                 1      1
     D  ££PAMI                 2      3  1
     D PGM             S             17     INZ('MUDRNRAPU001124_P')
     D MSG             S             50

     C                   EVAL      ££PAMI=5.2

     C                   EVAL      MSG=%CHAR(££PAMI)
     C     MSG           DSPLY

     C                   CALL      PGM
     C                   PARM                    ££PAMI

     C                   EVAL      MSG=%CHAR(££PAMI)
     C     MSG           DSPLY

     C                   SETON                                          LR