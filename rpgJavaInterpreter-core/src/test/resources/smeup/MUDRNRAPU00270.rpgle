     V* ==============================================================
     V* 13/11/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Define a DS with fields based on existing definitions
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error was about the CKEY not being resolved
     V* ==============================================================

     D CKEY            DS
     D  §OAVT1
     D  §OAVP1
     D  §OAVT2
     D  §OAVP2
     D  §OAVAT

     D  §OAVT1         S              2
     D  §OAVP1         S             10
     D  §OAVT2         S              2
     D  §OAVP2         S             10
     D  §OAVAT         S             15

     C                   EVAL §OAVT1='ok'
     C     CKEY.§OAVT1   DSPLY
     C                   SETON                                          LR
