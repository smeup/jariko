     V* ==============================================================
     V* 24/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * In this test we have a Fil and all fields are
    O *  placed on parent. So, the resolution must be in this place.
     V* ==============================================================
     FST01      IF   E           K DISK

     C                   EVAL      ST01_KEY='1'
     C     ST01_KEY      CHAIN     ST01
     C     ST01_KEY      DSPLY
     C     ST01_COL1     DSPLY
     C     ST01_COL2     DSPLY

     C                   SETON                                          LR

