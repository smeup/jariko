     V* ==============================================================
     V* 23/10/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Execute a subroutine that calls another subroutine and executes
    O * a GOTO redirecting to a TAG in the latter subroutine
     V* ==============================================================

     C                   EXSR      SR1
     C     1             IFEQ      0
     C     '3'           DSPLY
     C                   ENDIF
     C                   SETON                                          LR

     C     SR1           BEGSR
     C     1             IFEQ      1
     C     '1'           DSPLY
     C                   EXSR      SR2
     C     '2'           DSPLY
     C                   ENDIF
     C                   ENDSR

     C     SR2           BEGSR
     C     1             IFEQ      1
     C     '4'           DSPLY
     C                   GOTO      TAG_1
     C     '5'           DSPLY
     C                   ENDIF
     C     TAG_1         TAG
     C                   ENDSR
