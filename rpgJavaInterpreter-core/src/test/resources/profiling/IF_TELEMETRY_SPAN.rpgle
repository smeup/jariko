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
     C                   IF        1=1
      *@StartTrace M(IFBODY)
     C                   EVAL      RESULT = A + B * 8
     C                   EVAL      COUNT = COUNT + 1
      *@StopTrace M(IFBODY)
     C                   ELSEIF        1=2
      *@StartTrace M(ELIFBODY)
     C                   EVAL      RESULT = A + B * 8
     C                   EVAL      COUNT = COUNT + 1
      *@StopTrace M(ELIFBODY)
     C                   ELSE
      *@StartTrace M(ELSEBODY)
     C                   EVAL      RESULT = A + B * 8
     C                   EVAL      COUNT = COUNT + 1
      *@StopTrace M(ELSEBODY)
     C                   ENDIF
      *@StopTrace M(BEFORESTMT)
     C                   SETON                                        LR