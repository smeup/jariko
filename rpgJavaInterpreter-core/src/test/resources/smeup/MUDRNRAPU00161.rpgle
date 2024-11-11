     V* ==============================================================
     V* 11/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * MOVEL between a DS field without initialization to an
    O *  integer variable declared inline.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix:
    O *  `Issue executing MoveLStmt at line 19. For input
    O *   string: "   00000"`
     V* ==============================================================
     D DS1             DS             3
     D  DS1_D1                 1      3
     D TMP             S             10

     C                   EVAL      TMP=%CHAR(DS1_D1)
     C     TMP           DSPLY
     C                   MOVEL(P)  DS1_D1        INVAR             8 0          #Issue executing MoveLStmt at line 19. For input string: "   00000"
     C                   EVAL      TMP=%CHAR(INVAR)
     C     TMP           DSPLY

     C                   SETON                                          LR