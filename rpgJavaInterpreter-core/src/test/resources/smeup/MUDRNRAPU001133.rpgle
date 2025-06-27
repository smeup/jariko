     V* ==============================================================
     V* 27/06/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program reads a file through a procedure, by avoiding
    O *  the use of `SETLL` from procedure.
     V* ==============================================================
     FST03      IF   E           K DISK    RENAME(ST03RF:ST)
     DPR0              PR             1  0
     D                                1  0
     D KST03F1         S                   LIKE(ST03F1)
     D KST03F2         S                   LIKE(ST03F2)
     DPROC_VAL         S              1  0 INZ(1)

     C     KST           KLIST
     C                   KFLD                    KST03F1
     C                   KFLD                    KST03F2

     C                   EVAL      KST03F1='CNFOR'
     C     KST           SETLL     ST
     C                   READ      ST
     C     ST03F2        DSPLY
     C                   CALLP     PR0(PROC_VAL)
     C                   READ      ST
     C     ST03F2        DSPLY

     C                   SETON                                          LR


      * Procedure implementation
     PPR0              B
     DPR0              PI             1  0
     DPR0_F1                          1  0
      *
     C                   READ      ST
     C     ST03F2        DSPLY
     C                   RETURN    PR0_F1
      *
     PPR0              E