     V* ==============================================================
     V* 05/08/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Write to data areas
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, Jariko could not interpret data areas
     V* ==============================================================
     D SCAATTDS        DS           460
     C     *DTAARA       DEFINE    C£C£E00D      SCAATTDS
     C                   EVAL      SCAATTDS='WRITTEN'
     C                   OUT       SCAATTDS
     C     SCAATTDS      DSPLY
