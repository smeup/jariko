     V* ==============================================================
     V* 10/07/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Executes DB operation from procedure by using a file with
    O *  same Record Format name of another file imported with
    O *  `EXTNAME` for a DS.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *  Program FunctionInterpreter.PR0.static - Issue executing
    O *  SetgtStmt at line 46. Index 0 out of bounds for length 0
     V* ==============================================================
     FC5RREG1L  IF   E           K DISK
     DPR0              PR             1  0
     D                                1  0 CONST
     DVAL              S              1  0 INZ(2)
     DRES              S              1  0
     DC5RREG         E DS                  EXTNAME(C5RREG0F)

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
     C                   EVAL      RES=PR0(VAL)
     C     RES           DSPLY
     C                   SETON                                          LR


     PPR0              B
      *
     DPR0              PI             1  0
     DPR0_VAL1                        1  0       CONST
      *
     C     KRINI1        SETGT     C5RREGR                                      # Index 0 out of bounds for length 0
     C                   RETURN    PR0_VAL1*2
      *
     PPR0              E