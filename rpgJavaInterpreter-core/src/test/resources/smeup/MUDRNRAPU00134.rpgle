     V* ==============================================================
     V* 18/10/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Comparing number and `*ZEROS` by using `IFxx`.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was
    O *  `An operation is not implemented: Cannot compare
    O *    IntValue(value=4) to ZeroValue`.
     V* ==============================================================
     D FOO             S              1  0 INZ(4)

     C     FOO           IFGT      *ZEROS
     C     'OK'          DSPLY
     C                   ENDIF

     C                   SETON                                          LR      #An operation is not implemented: Cannot compare IntValue(value=4) to ZeroValue