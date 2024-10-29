     V* ==============================================================
     V* 24/10/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Use MONITOR with two ON-ERROR to catch a program call error
    O * The correct ON-ERROR clause is the first one
     V* ==============================================================
     C                   MONITOR
     C                   CALL      'MISSING'                                    £MON
     C                   ON-ERROR  00211
     C     'ok'          DSPLY
     C                   ON-ERROR  00101
     C     'ko'          DSPLY
     C                   ENDMON
