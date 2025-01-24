     V* ==============================================================
     V* 24/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * In this test we have a Data Structure declared as
    O *  not `QUALIFIED` by using `EXTNAME` keyword and a File to
    O *  the same resource declared for DS `EXTNAME`.
    O * In this case the File fields are removed from parent.
    O * The purpose of test is to check if DS field is resolved
    O *  without dot notation and refers to DS, and not to File.
     V* ==============================================================
     FST01      IF   E           K DISK
     D DS1           E DS                  EXTNAME(ST01) INZ

     C                   SETON                                          LR