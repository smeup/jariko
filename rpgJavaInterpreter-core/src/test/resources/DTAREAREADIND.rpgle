     V* ==============================================================
     V* 05/08/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Read from data areas with indicator
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, Jariko could not interpret data areas
     V* ==============================================================
     D SCAATTDS        DS           460
     D IND             S             20
     C     *DTAARA       DEFINE    C£C£E00D      SCAATTDS
     C                   EVAL      SCAATTDS='CURRENT'
     C                   IN        SCAATTDS                             50
     C                   EVAL      IND=*IN50
     C     SCAATTDS      DSPLY
     C     IND           DSPLY
