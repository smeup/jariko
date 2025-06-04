     V* ==============================================================
     V* 04/06/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Emit RPG Traces
     V* ==============================================================
     DA                S              8  0 INZ(5)
     DB                S              8  0 INZ(8)
     D RESULT          S              8  0 INZ(0)
     D COUNT           S              3  0
      *
  PROF* SPANSTART BEFORESTMT
     C                   MONITOR
  PROF* SPANSTART STMTBEGIN
     C                   CALL      'MISSING'                                    £MON
  PROF* SPANEND
     C                   ON-ERROR
  PROF* SPANSTART STMTERROR
     C     'ok'          DSPLY
  PROF* SPANEND
     C                   ENDMON
  PROF* SPANEND
     C                   SETON                                        LR