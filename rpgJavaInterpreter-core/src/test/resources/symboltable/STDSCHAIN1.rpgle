     V* ==============================================================
     V* 24/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * In this test we have a Data Structure declared as not
    O *  `QUALIFIED` by using `EXTNAME` keyword and a File to the same
    O *  declared for DS `EXTNAME`. In this case the File fields are
    O *  removed from parent of Symbol Table.
    O * Like for `CHAIN` of this test, every field must be
    O *  resolved in DS.
     V* ==============================================================
     FST01      IF   E           K DISK
     D DS1           E DS                  EXTNAME(ST01) INZ

     C                   EVAL      ST01_KEY='1'
     C     ST01_KEY      CHAIN     ST01
     C     ST01_KEY      DSPLY
     C     ST01_COL1     DSPLY
     C     ST01_COL2     DSPLY

     C                   SETON                                          LR

