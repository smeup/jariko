     V* ==============================================================
     V* 03/09/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Read from data areas with *LOCK
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, Jariko could not interpret data areas
     V* ==============================================================
     D SCAATTDS        DS           460
     C     *DTAARA       DEFINE    C£C£E00D      SCAATTDS
     C                   EVAL      SCAATTDS='CURRENT'
     C     *LOCK         IN        SCAATTDS
     C     SCAATTDS      DSPLY
