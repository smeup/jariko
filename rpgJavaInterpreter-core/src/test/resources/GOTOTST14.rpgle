     V* ==============================================================
     V* 02/12/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Execute a SELECT with a GOTO to a TAG in its first branch
     V* ==============================================================
     C                   GOTO      BGN
     C                   SELECT
     C                   WHEN      1=2
     C     'ko'          DSPLY
     C                   WHEN      2=3
     C     BGN           TAG
     C     '1'           DSPLY
     C                   WHEN      3=4
     C     'ko'          DSPLY
     C                   OTHER
     C     'ko'          DSPLY
     C                   ENDSL
     C     '2'           DSPLY
