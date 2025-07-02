     V* ==============================================================
     V* 27/06/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program reads a file by using `SETLL` and `READ`; then
    O *  two `READ` without `SETLL`.
     V* ==============================================================
     FST02      IF   E           K DISK    RENAME(ST02RF:ST)
     D KST02F1         S                   LIKE(ST02F1)
     D KST02F2         S                   LIKE(ST02F2)

     C     KST           KLIST
     C                   KFLD                    KST02F1
     C                   KFLD                    KST02F2

     C                   EVAL      KST02F1='CNFOR'

     C     KST           SETLL     ST
     C                   READ      ST
     C     ST02F2        DSPLY

     C                   READ      ST
     C     ST02F2        DSPLY

     C                   READ      ST
     C     ST02F2        DSPLY

     C                   SETON                                          LR