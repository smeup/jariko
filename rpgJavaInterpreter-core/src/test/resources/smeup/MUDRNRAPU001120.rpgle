     V* ==============================================================
     V* 16/04/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Using `SETGT` and `READ` in a cycle. The cycle is interrupted
    O *  when the EOF had been reached.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *   %EOF expression with file names is not implemented yet
     V* ==============================================================
     FST02      IF   E           K DISK
     D KST02F1         S                   LIKE(ST02F1)
     D KST02F2         S                   LIKE(ST02F2)
     D I               S              1  0

     C     KRIS12        KLIST
     C                   KFLD                    KST02F1
     C                   KFLD                    KST02F2

     C                   EVAL      KST02F1='CN'

     C                   FOR       I=1 TO 4
     C     KRIS12        SETGT     ST02
     C                   READ      ST02
     C                   IF        %EOF(ST02)                                   #  %EOF expression with file names is not implemented yet
     C     'LEAVE'       DSPLY
     C                   LEAVE
     C                   ENDIF
     C     ST02F2        DSPLY
     C                   EVAL      KST02F1=ST02F1
     C                   EVAL      KST02F2=ST02F2
     C                   ENDFOR

     C                   SETON                                          LR