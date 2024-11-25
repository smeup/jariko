     V* ==============================================================
     V* 25/11/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Execute a subroutine with a CALL to a program performing a top-level GOTO
     V* ==============================================================
     C                   EXSR      SR1
     C                   SETON                                          LR

     C     SR1           BEGSR
     C                   CALL      'GOTOTST10B'
     C     'ok'          DSPLY
     C                   ENDSR
