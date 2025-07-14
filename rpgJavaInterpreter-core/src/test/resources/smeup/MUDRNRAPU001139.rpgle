     V* ==============================================================
     V* 10/07/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Try to use SETGT by specifying the `recordFormat`.
    O * Also, there are two file with same `recordFormat` but with
    O *  `accessFields` with different size.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *  Program MUDRNRAPU001139 - Issue executing SetgtStmt at
    O *  line 32. Index 5 out of bounds for length 5
     V* ==============================================================
     FC5RREG1L  IF   E           K DISK
     FC5RREG7L  IF   E           K DISK    RENAME(C5RREGR:C5RREG7)

     C     KRINI1        KLIST
     C                   KFLD                    KRAZIE
     C                   KFLD                    KRESER
     C                   KFLD                    KRCONT
     C                   KFLD                    KRTPCN
     C                   KFLD                    KRSOGG
     C                   KFLD                    KRDREG
      *
     C     *LIKE         DEFINE    R5AZIE        KRAZIE
     C     *LIKE         DEFINE    R5ESER        KRESER
     C     *LIKE         DEFINE    R5CONT        KRCONT
     C     *LIKE         DEFINE    R5TPCN        KRTPCN
     C     *LIKE         DEFINE    R5SOGG        KRSOGG
     C     *LIKE         DEFINE    R5DREG        KRDREG
      *
     C     KRINI1        SETGT     C5RREGR
     C                   SETON                                          LR