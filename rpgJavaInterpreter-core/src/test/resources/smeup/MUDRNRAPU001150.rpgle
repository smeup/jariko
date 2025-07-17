     V* ==============================================================
     V* 16/07/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program shows the message on a true case of `IFGE  when
    O *  a decimal value is compared to `*ZEROS`.
     V* ==============================================================
     D VAL             S              3  2 INZ(0.00)

     C     VAL           IFGE      *ZEROS
     C     'GE VAL'      DSPLY
     C                   ENDIF

     C     *ZEROS        IFGE      VAL
     C     'GE ZERO'     DSPLY
     C                   ENDIF

     C                   SETON                                          LR