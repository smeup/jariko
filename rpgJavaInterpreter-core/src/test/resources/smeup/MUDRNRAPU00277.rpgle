     V* ==============================================================
     V* 05/12/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Define a field of a DS defined with a DIM based on a d-spec constant
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, jariko throw a data reference error
     V* ==============================================================
     D                 DS
     D AZISCH                       512    DIM(NUMAZI)
     D NUMAZI          C                   58
     C     'ok'          DSPLY
