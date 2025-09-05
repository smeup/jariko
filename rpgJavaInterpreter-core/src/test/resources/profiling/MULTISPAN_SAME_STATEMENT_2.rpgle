     V* ==============================================================
     V* 30/05/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Emit RPG Traces
     V* ==============================================================
   COP* *TRACE
     DA                S              8  0 INZ(5)
     DB                S              8  0 INZ(8)
     D RESULT          S              8  0 INZ(0)
      *
      *@StartTrace M(_SPANID1) "COMMENT"
      *@StartTrace M(_SPANID2) "COMMENT"
      *@StartTrace M(_SPANID3) "COMMENT"
      *@StartTrace M(_SPANID4) "COMMENT"
     C                   EVAL      RESULT = A + B
      *@StopTrace M(_SPANID4)
      *@StopTrace M(_SPANID3)
      *@StopTrace M(_SPANID2)
     C                   EVAL      RESULT = RESULT + 1
      *@StopTrace M(_SPANID1)
     C                   SETON                                        LR
