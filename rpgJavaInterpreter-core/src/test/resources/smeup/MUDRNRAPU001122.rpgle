     V* ==============================================================
     V* 05/06/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Calling a program by passing Decimal to a DS which defines
    O *  only one field as Decimal.
     V* ==============================================================
     D B£2$DS          DS           100
     D  ££UMHR                 1      1
     D  ££PAMI                 2      3  1
     D PGM             S             17     INZ('MUDRNRAPU001122_P')

     C                   EVAL      ££PAMI=5.2

     C                   CALL      PGM
     C                   PARM                    ££PAMI

     C     ££PAMI        DSPLY

     C                   SETON                                          LR