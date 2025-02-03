     V* ==============================================================
     V* 31/01/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Utilization of a field (declared from unqualified DS)
    O *  on main program and a variable, with same name, declared as
    O *  Standalone on API program.
     V* ==============================================================
     D                 DS
     D  VAR1                   1     10

     C                   EVAL      VAR1='FOO'
     C                   EXSR      APISR
     C     VAR2          DSPLY

      /API QILEGEN,MUDRNRAPU00191_API

     C                   SETON                                          LR