     V* ==============================================================
     V* 25/10/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Execute a subroutine with a GOTO redirecting to the end
     V* ==============================================================
     C                   EXSR      SR1
     C     '2'           DSPLY
     C                   SETON                                          LR

     C     SR1           BEGSR
     C                   GOTO      G9SR1
     C     '1'           DSPLY
     C     G9SR1         ENDSR
