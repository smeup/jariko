     V* ==============================================================
     V* 18/02/2025 APU001 Creation
     V* 19/02/2025 APU001 Applied simplification by removing `RENAME`
     V* ==============================================================
    O * PROGRAM GOAL
    O * Using `SETGT` and `READ` in a cycle.
     V* ==============================================================
     FST02      IF   E           K DISK
     D KST02F1         S                   LIKE(ST02F1)
     D KST02F2         S                   LIKE(ST02F2)
     D I               S              1  0

     C     KRIS12        KLIST
     C                   KFLD                    KST02F1
     C                   KFLD                    KST02F2

     C                   EVAL      KST02F1='CN'

     C                   FOR       I=1 TO 3
     C     KRIS12        SETGT     ST02
     C                   READ      ST02                                   35
     C     ST02F2        DSPLY
     C                   EVAL      KST02F1=ST02F1
     C                   EVAL      KST02F2=ST02F2
     C                   ENDFOR

     C                   SETON                                          LR