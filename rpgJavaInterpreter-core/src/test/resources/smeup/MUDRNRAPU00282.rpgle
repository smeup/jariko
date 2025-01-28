     V* ==============================================================
     V* 24/01/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * *LIKE DEFINE referencing a DS must be defined as
    0 * strings with the same size as the DS
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, they were defined as DS themselves
     V* ==============================================================
     D $A              DS           512
     C     *LIKE         DEFINE    $A            $B