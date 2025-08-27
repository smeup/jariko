     V* ==============================================================
     V* 26/08/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Write to not defined data area
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, Jariko threw a difficult to interpret NullPointerException
     V* ==============================================================
     D SCAATTDS        DS           460
     C*     *DTAARA       DEFINE    C£C£E00D      SCAATTDS
     C                   EVAL      SCAATTDS='CURRENT'
     C                   OUT       SCAATTDS
     C     SCAATTDS      DSPLY
