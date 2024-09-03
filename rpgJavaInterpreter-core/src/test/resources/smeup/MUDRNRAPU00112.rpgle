     V* ==============================================================
     V* 03/09/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Assignment, with Z-ADD, the content of S (declared inline)
    O * to a DS field.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was
    O *  Cannot coerce sub-string `      `.
     V* ==============================================================
     D DS1             DS           100
     D  DS1_FL1                1      6  0
     D  DS1_FL2                7     12  0

     D TMP             S             10

     C                   CLEAR                   DS1

     C     DS1_FL1       DSPLY                                                  #Cannot coerce sub-string `      `
     C     STD8          DSPLY

     C                   Z-ADD     DS1_FL1       STD8
     C                   Z-ADD     STD8          STD8              8 0

     C     DS1_FL1       DSPLY
     C     STD8          DSPLY