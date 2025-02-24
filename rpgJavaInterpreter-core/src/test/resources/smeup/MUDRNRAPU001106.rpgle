     V* ==============================================================
     V* 24/02/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program shows the `300.000000` Packed number of a DS.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *     `Character ` ` is neither a decimal digit number, decimal
    O *      point, nor "e" notation exponential mark`
     V* ==============================================================
     D DS1             DS
     D DS1_FL1                       15P 6

     C                   EVAL       DS1_FL1='300.000000'
     C     DS1_FL1       DSPLY                                                  # Character ` ` is neither a decimal digit number, decimal point, nor "e" notation exponential mark

     C                   SETON                                        RT