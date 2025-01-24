     V* ==============================================================
     V* 24/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * In this test we have a Data Structure declared as not
    O *  `QUALIFIED` by using `EXTNAME` keyword.
    O * When I use a set (by `EVAL`) for a field without dot notation,
    O *   the parent DS must be find in parent of Symbol Table.
     V* ==============================================================
     D DS1           E DS                  EXTNAME(ST01) INZ

     C                   EVAL      ST01_COL1='FOO'
     C     ST01_COL1     DSPLY

     C                   SETON                                          LR

