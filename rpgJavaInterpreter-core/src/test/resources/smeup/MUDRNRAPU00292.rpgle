     V* ==============================================================
     V* 16/06/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Call statement with indicator throwing an external error
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, jariko reported a null error
     V* ==============================================================
     D MSG             S          30000    VARYING
     C                   CALL      'DOPEDPGM'                           37
     C                   EVAL      MSG='ok'
     C     MSG           DSPLY
