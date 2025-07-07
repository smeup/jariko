     V* ==============================================================
     V* 01/07/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program reads a file through a nested procedure call.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *   `Issue executing SetllStmt at line 48.
    O *    Cannot find searchedValued for KST02F1`
     V* ==============================================================
     FST02      IF   E           K DISK    RENAME(ST02RF:ST)
     DPR0              PR             1  0
     D                                1  0
     DPR1              PR             1  0
     D                                1  0
     D KST02F1         S                   LIKE(ST02F1)
     D KST02F2         S                   LIKE(ST02F2)
     DPROC_VAL         S              1  0 INZ(1)

     C     KST           KLIST
     C                   KFLD                    KST02F1
     C                   KFLD                    KST02F2

     C                   EVAL      KST02F1='CNFOR'
     C     KST           SETLL     ST
     C                   READ      ST
     C     ST02F2        DSPLY
     C                   CALLP     PR0(PROC_VAL)

     C                   SETON                                          LR


      * Procedure implementation
     PPR0              B
     DPR0              PI             1  0
     DPR0_F1                          1  0
      *
     C                   RETURN    PR1(PROC_VAL)
      *
     PPR0              E

     PPR1              B
     DPR1              PI             1  0
     DPR1_F1                          1  0
      *
     C                   EVAL      KST02F1='CNFOR'
     C     KST           SETLL     ST
     C                   READ      ST
     C     ST02F2        DSPLY
     C                   RETURN    PR1_F1
      *
     PPR1              E