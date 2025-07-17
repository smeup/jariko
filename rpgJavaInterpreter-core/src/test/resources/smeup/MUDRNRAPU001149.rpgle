     V* ==============================================================
     V* 16/07/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program shows the message on a true case of `IFNE  when
    O *  a decimal value is compared to `*ZEROS`.
     V* ==============================================================
     D VAL             S              2  1 INZ(1.0)

     C     VAL           IFNE      *ZEROS
     C     'NE VAL'      DSPLY
     C                   ENDIF

     C     *ZEROS        IFNE      VAL
     C     'NE ZERO'     DSPLY
     C                   ENDIF

     C                   SETON                                          LR