     V* ==============================================================
     V* 23/10/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Execute a nested GOTO in a subroutine redirecting to a TAG
    O * in top level
     V* ==============================================================

     C                   EXSR      SR
     C     TAG_1         TAG
     C                   SETON                                          LR

     C     SR            BEGSR
     C     1             IFEQ      1
     C     '1'           DSPLY
     C                   GOTO      TAG_1
     C     '2'           DSPLY
     C                   ENDIF
     C                   ENDSR
