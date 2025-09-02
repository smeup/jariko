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
      *@StartTrace M(BEFOREDO)
     C                   DO        100
      *@StartTrace M(DOBODYSTART)
     C                   EVAL      RESULT = A + B * 8
      *@StopTrace M(DOBODYSTART)
     C                   ENDDO
      *@StopTrace M(BEFOREDO)
     C                   SETON                                        LR