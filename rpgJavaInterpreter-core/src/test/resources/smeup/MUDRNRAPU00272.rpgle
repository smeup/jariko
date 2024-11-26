     V* ==============================================================
     V* 26/11/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Perform READE operation with EQ indicator
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, indicators on READE operations were ignored
     V* ==============================================================
     FC5ADFF9L  IF   E           K DISK    RENAME(C5ADFFR:C5ADFF9)
     C     TAG1          TAG
     C     KSCEN         READE     C5ADFF9L                               50
     C  N50              IF        *ON
     C                   GOTO      TAG1
     C                   ENDIF
     C     'ok'          DSPLY
     C     *LIKE         DEFINE    D5SCEN        KSCEN