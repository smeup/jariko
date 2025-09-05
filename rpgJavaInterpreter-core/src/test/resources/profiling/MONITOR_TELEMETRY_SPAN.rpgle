     V* ==============================================================
     V* 04/06/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Emit RPG Traces
     V* ==============================================================
   COP* *TRACE
     DA                S              8  0 INZ(5)
     DB                S              8  0 INZ(8)
     D RESULT          S              8  0 INZ(0)
     D COUNT           S              3  0
      *
      *@StartTrace M(BEFORESTMT)
     C                   MONITOR
      *@StartTrace M(STMTBEGIN)
     C                   CALL      'MISSING'                                    £MON
      *@StopTrace M(STMTBEGIN)
     C                   ON-ERROR
      *@StartTrace M(STMTERROR)
     C     'ok'          DSPLY
      *@StopTrace M(STMTERROR)
     C                   ENDMON
      *@StopTrace M(BEFORESTMT)
     C                   SETON                                        LR