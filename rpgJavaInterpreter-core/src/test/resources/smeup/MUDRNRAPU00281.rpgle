     V* ==============================================================
     V* 21/01/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Definitions with LIKE referencing a DS must be defined as
    0 * strings with the same size as the DS
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, they were defined as DS themselves
     V* ==============================================================
     FMULANGTL  IF   E           K DISK
     D ML            E DS                  EXTNAME(MULANGTL) INZ
     D DS0002          S                   LIKE(ML)