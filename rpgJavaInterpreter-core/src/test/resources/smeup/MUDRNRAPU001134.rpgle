     V* ==============================================================
     V* 27/06/2025 APU001 Creation
     V* 30/06/2025 APU001 Changed file
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program reads a file by using `SETLL` and `READ`, then
    O *  two `READ` without `SETLL`.
     V* ==============================================================
     FST03      IF   E           K DISK    RENAME(ST03RF:ST)
     D KST03F1         S                   LIKE(ST03F1)
     D KST03F2         S                   LIKE(ST03F2)

     C     KST           KLIST
     C                   KFLD                    KST03F1
     C                   KFLD                    KST03F2

     C                   EVAL      KST03F1='CNFOR'

     C     KST           SETLL     ST
     C                   READ      ST
     C     ST03F2        DSPLY

     C                   READ      ST
     C     ST03F2        DSPLY

     C                   READ      ST
     C     ST03F2        DSPLY

     C                   SETON                                          LR