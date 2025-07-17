     V* ==============================================================
     V* 16/07/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program shows the message on a true case of `IFLT` when
    O *  a decimal value is compared to `*ZEROS`.
     V* ==============================================================
     D VAL             S              2  1 INZ(-1.0)

     C     VAL           IFLT      *ZEROS
     C     'LT VAL'      DSPLY
     C                   ENDIF

     C                   EVAL      VAL=1.0
     C     *ZEROS        IFLT      VAL
     C     'LT ZERO'     DSPLY
     C                   ENDIF

     C                   SETON                                          LR