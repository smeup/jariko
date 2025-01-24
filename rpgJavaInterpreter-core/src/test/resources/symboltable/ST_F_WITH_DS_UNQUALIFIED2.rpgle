     V* ==============================================================
     V* 24/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * In this test we have a Data Structure declared as
    O *  not `QUALIFIED` and a File.
    O * In this case the File fields are present in root.
    O * The purpose of test is to check File field resolution.
     V* ==============================================================
     FST01      IF   E           K DISK
     D DS1             DS                  INZ
     D  DS1_F1                        3
     D  DS1_F2                        3

     C                   SETON                                          LR