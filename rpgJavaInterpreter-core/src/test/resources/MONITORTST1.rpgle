     V* ==============================================================
     V* 24/10/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Use MONITOR with two ON-ERROR to catch a program call error
     V* ==============================================================
     C                   MONITOR
     C                   CALL      'MISSING'                                    £MON
     C                   ON-ERROR
     C     'ok'          DSPLY
     C                   ENDMON
