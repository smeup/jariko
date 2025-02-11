     V* ==============================================================
     V* 11/02/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * *LIKE DEFINE referencing an indicator turned on with an eval
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, jariko could not define XIN10
     V* ==============================================================
     C                   EVAL      *IN10=*ON
     C     *LIKE         DEFINE    *IN10         XIN10
     C     'ok'          DSPLY