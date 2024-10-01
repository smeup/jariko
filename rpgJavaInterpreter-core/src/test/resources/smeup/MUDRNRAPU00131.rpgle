     V* ==============================================================
     V* 30/09/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Access to a DS numeric field not initialized. The value,
    O *  on Jariko, must be 0.
     V* ==============================================================
     D DS01            DS
     D  DS01_F1                       8  2
     D  DS01_F2                      10  0

     D MSG             S             20

     C                   EVAL      MSG=%CHAR(DS01_F1)
     C     MSG           DSPLY

     C                   EVAL      MSG=%CHAR(DS01_F2)
     C     MSG           DSPLY

     C                   SETON                                          LR