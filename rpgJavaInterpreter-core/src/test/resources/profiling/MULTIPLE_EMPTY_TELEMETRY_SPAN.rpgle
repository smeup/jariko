     V* ==============================================================
     V* 13/05/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Emit RPG Traces
     V* ==============================================================
   COP* *TRACE
     DA                S              8  0 INZ(5)
     DB                S              8  0 INZ(8)
     D RESULT          S              8  0 INZ(0)
      *
      *@StartTrace M(_EMPTY)
      *@StartTrace M(_EMPTY2)
      *@StopTrace M(_EMPTY2)
      *@StartTrace M(_EMPTY3)
      *@StopTrace M(_EMPTY3)
      *@StopTrace M(_EMPTY)
      *@StartTrace M(_SPANID1)
      *@StartTrace M(_EMPTY4)
      *@StartTrace M(_EMPTY5)
      *@StartTrace M(_EMPTY6)
      *@StopTrace M(_EMPTY6)
      *@StopTrace M(_EMPTY5)
      *@StopTrace M(_EMPTY4)
      *@StartTrace M(_EMPTY7)
      *@StopTrace M(_EMPTY7)
     C                   EVAL      RESULT = A + B
      *@StartTrace M(_EMPTY8)
      *@StopTrace M(_EMPTY8)
      *@StopTrace M(_SPANID1)
     C                   SETON                                        LR
      *@StartTrace M(_EMPTY9)
      *@StopTrace M(_EMPTY9)