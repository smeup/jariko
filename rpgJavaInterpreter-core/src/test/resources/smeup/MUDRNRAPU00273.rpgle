     V* ==============================================================
     V* 28/11/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * DEFINE on an indicator
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, jariko throw a DataReference error
     V* ==============================================================
     C                   SETON                                        10
     C                   EVAL      XIN10=*ON
     C     XIN10         DSPLY
     C                   EVAL      XIN10=*OFF
     C     XIN10         DSPLY
     C     *LIKE         DEFINE    *IN10         XIN10
