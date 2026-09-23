     V* ==============================================================
     V* 18/09/2026 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Regression for smeup/jariko#823: a positional binary (B) DS
    O * subfield spanning 4 bytes must round-trip a value above 65535
    O * (a byte span of 4 was mistakenly treated as a decimal digit
    O * count, giving the field only 2 bytes of storage instead of 4,
    O * silently truncating any value above 65535).
     V* ==============================================================
     D DSTEST          DS           400
     D BIGVAL                397    400B 0

     C                   EVAL      BIGVAL = 100000
     C     BIGVAL        DSPLY
     C                   SETON                                        LR
