     V* ==============================================================
     V* 16/05/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Extract a VARYING field from a DS
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, jariko did not consider the VARYING length
     V* ==============================================================
     D £JaxDSGen       DS
     D £JaxCP                     30000    INZ VARYING
     D MSG            S           30000    VARYING
     C                   EVAL      £JaxCP='ABCDE'
     C                   EVAL      MSG=£JaxCP
     C                   EVAL      MSG=%LEN(MSG)
     C     MSG           DSPLY
