     V* ==============================================================
     V* 05/09/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Comparing String and Boolean with `COMP` operator
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *  Cannot compare StringValue to BooleanValue.
     V* ==============================================================
     D £C5G35          S              1

     C                   MOVEL     *ON           £C5G35
     C     £C5G35        COMP      *ON                                    35    # An operation is not implemented: Cannot compare StringValue[1](1) to BooleanValue(value=true),
     C     *IN35         DSPLY

     C                   MOVEL     *OFF          £C5G35
     C     £C5G35        COMP      *ON                                    35
     C     *IN35         DSPLY