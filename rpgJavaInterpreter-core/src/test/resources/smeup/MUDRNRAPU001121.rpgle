     V* ==============================================================
     V* 05/06/2025 APU001 Creation
     V* 06/06/2025 APU001 Edit
     V* ==============================================================
    O * PROGRAM GOAL
    O * Calling a program by passing Integer to a DS which defines
    O *  only one field as Integer.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *   `param DSPAMI was expected to have type DataStructureType.
    O *    [...] It has value: IntValue(value=5))`
     V* ==============================================================
     D B£2$DS          DS           100
     D  ££UMHR                 1      1
     D  ££PAMI                 2      3  0
     D PGM             S             17     INZ('MUDRNRAPU001121_P')
     D MSG             S             50

     C                   EVAL      ££PAMI=5

     C                   EVAL      MSG=%CHAR(££PAMI)
     C     MSG           DSPLY

     C                   CALL      PGM
     C                   PARM                    ££PAMI

     C                   EVAL      MSG=%CHAR(££PAMI)
     C     MSG           DSPLY

     C                   SETON                                          LR