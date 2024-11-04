     V* ==============================================================
     D* 24/10/24
     D* Purpose: Must fire the following errors during execution of
     D* C                   CALL      'MISSING'
     D* line 8 - "Error calling program or procedure - Could not find program MISSING"
     V* ==============================================================
     C                   MONITOR
     C                   CALL      'MISSING'                                    £MON
     C                   ON-ERROR 00101
     C     'ok'          DSPLY
     C                   ENDMON
