     V* ==============================================================
     V* 05/06/2025 APU002 Creation
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
      *@StartTrace M(BEFORESELECT)
     C                   SELECT
     C                   WHEN      COUNT=1
      *@StartTrace M(CONTENT1)
     C                   EVAL      RESULT = A + B * 1
      *@StopTrace M(CONTENT1)
     C                   WHEN      COUNT=2
      *@StartTrace M(CONTENT2)
     C                   EVAL      RESULT = A + B * 2
      *@StopTrace M(CONTENT2)
     C                   OTHER
      *@StartTrace M(CONTENT3)
     C                   EVAL      RESULT = A + B * 3
      *@StopTrace M(CONTENT3)
     C                   ENDSL
      *@StopTrace M(BEFORESELECT)
     C                   SETON                                        LR