     V* ==============================================================
     V* 26/06/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program reads a file through a procedure.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *   `Line: 36 - File definition ST not found`
     V* ==============================================================
     FST02      IF   E           K DISK    RENAME(ST02RF:ST)
     DPR0              PR             1  0
     D                                1  0
     D KST02F1         S                   LIKE(ST02F1)
     D KST02F2         S                   LIKE(ST02F2)
     D PROC_VAL        C                   CONST(1)

     C     KST           KLIST
     C                   KFLD                    KST02F1
     C                   KFLD                    KST02F2

     C                   EVAL      KST02F1='CNFOR'
     C     KST           SETLL     ST
     C     KST           READ      ST
     C     ST02F2        DSPLY
     C                   CALLP     PR0(PROC_VAL)

     C                   SETON                                          LR


      * Procedure implementation
     PPR0              B
     DPR0              PI             1  0
     DPR0_F1                          1  0
      *
     C                   EVAL      KST02F1='CNFOR'
     C     KST           SETLL     ST
     C     KST           READ      ST
     C     ST02F2        DSPLY
     C                   RETURN    PR0_F1
      *
     PPR0              E