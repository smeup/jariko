     V* ==============================================================
     V* 03/12/2024 BERNI  Creation
     V* 03/12/2024 APU001 Edit by removing COPY for compatibility
     V*                   on Jariko
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program is more complex. Tests the assignment of value
    O *  between waterfall calls and by parameters. Each subprogram is
    O *  called max 101 times (last is Jariko error), thanks `DO`
    O *  statement; this main program calls `MUDRNRAPU00175_P1` which
    O *  calls recursively `MUDRNRAPU00175_P2`.
     V* ==============================================================
     DXXARRAY          S              1    DIM(100) INZ
     D PGM_NAME        S             17    INZ('MUDRNRAPU00175_P1')
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * Empty Main
      *
     C                   SETON                                        RT
      *---------------------------------------------------------------
    RD* INITIAL ROUTINE
      *--------------------------------------------------------------*
     C     *INZSR        BEGSR
      *
     C                   DO        *HIVAL        $X                5 0
      * Cycling on MUDRNRAPU00175_P1 program
     C                   CALL      PGM_NAME
     C     *IN35         PARM                    ££35              1
      * If indicator 35 is turned on, the cycle is interrupted.
     C   35              LEAVE
      *
      * . else, assign the indicator value to array.
     C                   EVAL      XXARRAY($X)=££35
      *
     C                   ENDDO
      *
     C     'HELLO'       DSPLY
      *
     C                   ENDSR
