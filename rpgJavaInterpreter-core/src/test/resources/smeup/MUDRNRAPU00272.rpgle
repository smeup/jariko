     V* ==============================================================
     V* 26/11/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Perform READ operations with EQ indicator
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, indicators on READ operations were ignored
     V* ==============================================================
     FC5ADFF9L  IF   E           K DISK    RENAME(C5ADFFR:C5ADFF9)
     C* READE with eof indicator
     C     TAG1          TAG
     C     KSCEN         READE     C5ADFF9L                               50
     C  N50              IF        *ON
     C     'ko'          DSPLY
     C                   GOTO      TAG1
     C                   ENDIF
     C     'ok'          DSPLY
     C* READPE bof indicator
     C     *LOVAL        SETLL     C5ADFF9L
     C     TAG2          TAG
     C     KSCEN         READPE    C5ADFF9L                               50
     C  N50              IF        *ON
     C     'ko'          DSPLY
     C                   GOTO      TAG2
     C                   ENDIF
     C     'ok'          DSPLY
     C* READ eof indicator
     C     *HIVAL        SETLL     C5ADFF9L
     C     TAG3          TAG
     C     KSCEN         READ      C5ADFF9L                               50
     C  N50              IF        *ON
     C     'ko'          DSPLY
     C                   GOTO      TAG3
     C                   ENDIF
     C     'ok'          DSPLY
     C* READP bof indicator
     C     *LOVAL        SETLL     C5ADFF9L
     C     TAG4          TAG
     C     KSCEN         READP     C5ADFF9L                               50
     C  N50              IF        *ON
     C     'ko'          DSPLY
     C                   GOTO      TAG4
     C                   ENDIF
     C     'ok'          DSPLY
     C*
     C     *LIKE         DEFINE    D5SCEN        KSCEN