     V* ==============================================================
     V* 12/09/2024 APU011 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Verifies the conversion of values to a data structure when
    O * using the UnlimitedStringType and displays the results.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O * Conversion to data struct value not implemented for UnlimitedStringType
     V* ==============================================================

     D DSMETO          DS
     D  §FUNZ                         2
     D  §METO                         2

     D £UIBDS          DS             10
     D £UIBME                          0    INZ('test')

     C                   MOVEL(P)  £UIBME        DSMETO

     C     DSMETO        DSPLY
     C     §FUNZ         DSPLY
     C     §METO         DSPLY


