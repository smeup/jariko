     V* ==============================================================
     V* 24/10/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Use MONITOR with two ON-ERROR to catch a program call error
    O * Using an *ALL clause
     V* ==============================================================
     C                   MONITOR
     C                   CALL      'MISSING'                                    £MON
     C                   ON-ERROR *ALL
     C     'ok'          DSPLY
     C                   ENDMON
