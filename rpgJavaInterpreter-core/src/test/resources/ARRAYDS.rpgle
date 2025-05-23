     V* ==============================================================
     V* 08/04/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Perform a CALL passing an array value to a field received as DS
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, jariko crashed
     V* ==============================================================
     D £C6MIM          DS
     D  £6I                          20P 6 DIM(99) INZ
     C                   CALL      'ARRAYDS_1'
     C                   PARM                    £6I
     C     £6I           DSPLY
     C                   SETON                                          LR