     V* ==============================================================
     V* 08/04/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Get an array in PLIST for a DS variable, called from ARRAYDS
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, jariko crashed
     V* ==============================================================
     D £C6MIM          DS
     D  £6I                          20P 6 DIM(99) INZ
     DXC6MIM           S                   LIKE(£C6MIM)
     C                   EVAL      XC6MIM=£C6MIM
     C     *ENTRY        PLIST
     C                   PARM                    XC6MIM
     C                   SETON                                          LR