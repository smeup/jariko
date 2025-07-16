     V* ==============================================================
     V* 16/07/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program shows the message on a true case of `IFEQ` when
    O *  a decimal value is compared to `*ZEROS`.
     V* ==============================================================
     D VAL             S              3  1 INZ(0.0)

     C     VAL           IFEQ      *ZEROS
     C     'EQ VAL'      DSPLY
     C                   ENDIF

     C     *ZEROS        IFEQ      VAL
     C     'EQ ZERO'     DSPLY
     C                   ENDIF

     C                   SETON                                          LR