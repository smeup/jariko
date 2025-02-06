     V* ==============================================================
     V* 04/12/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * CHECKR with indexed expression based on a Data Reference
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, jariko throw a syntax error
     V* ==============================================================
     DN2               S              2  0
     D T$C5MD          S             15
     DA1               S              2
     C     A1            CHECKR    T$C5MD:N2     N2
     C     'ok'          DSPLY