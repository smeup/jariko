     V* ==============================================================
     V* 10/03/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Z-ADD on a from a binary field to a binary data definition
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, we had an assignment error
     V* ==============================================================
     D DSREC0          DS
     D  X0NREC               397    400B 0

     C     X0NREC        DSPLY

     C                   Z-ADD     X0NREC        XXNREC
     C     XXNREC        DSPLY
     C     *LIKE         DEFINE    X0NREC        XXNREC
     C                   SETON                                        RT