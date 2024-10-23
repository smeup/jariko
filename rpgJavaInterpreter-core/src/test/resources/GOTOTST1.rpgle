     V* ==============================================================
     V* 23/10/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Execute a GOTO in a subroutine redirecting to a TAG nested
    O * in the same subroutine
     V* ==============================================================

     C                   EXSR      SR
     C                   SETON                                          LR

     C     SR            BEGSR
     C                   GOTO      TAG_1
     C     1             IFEQ      1
     C     '1'           DSPLY
     C     TAG_1         TAG
     C     '2'           DSPLY
     C                   ENDIF
     C                   ENDSR
