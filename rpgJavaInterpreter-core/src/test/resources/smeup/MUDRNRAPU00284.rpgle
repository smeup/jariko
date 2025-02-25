     V* ==============================================================
     V* 25/02/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Decode a packed encoded with a scale smaller than what its
    O * type expects
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, we had an out of bound error
     V* ==============================================================
     DC5RITE         E DS                  EXTNAME(C5RITE0F) INZ
     C                   EVAL      P5IMNS = '.010000'
     C     P5IMNS        DSPLY

