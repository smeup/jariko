     V* ==============================================================
     V* 16/07/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program shows the message on a true case of `IFGT` when
    O *  a decimal value is compared to `*ZEROS`.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *  Program MUDRNRAPU001146 - Issue executing IfStmt at line 19.
    O *  An operation is not implemented: Cannot compare
    O *   DecimalValue(value=1.0) to ZeroValue
    O *  Program MUDRNRAPU001146 - Issue executing IfStmt at line 23.
    O *  An operation is not implemented: Cannot compare ZeroValue to
    O *   DecimalValue(value=-1.0)
     V* ==============================================================
     D VAL             S              2  1 INZ(1.0)

     C     VAL           IFGT      *ZEROS
     C     'GT VAL'      DSPLY
     C                   ENDIF

     C                   EVAL      VAL=-1.0
     C     *ZEROS        IFGT      VAL
     C     'GT ZERO'     DSPLY
     C                   ENDIF

     C                   SETON                                          LR