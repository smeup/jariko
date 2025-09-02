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
     D COUNT           S              3  0
      *
      *@StartTrace M(BEFOREFOR)
     C                   FOR       COUNT = 10 DOWNTO 1
      *@StartTrace M(FORBODYSTART)
     C                   EVAL      RESULT = A + B * 8
      *@StopTrace M(FORBODYSTART)
     C                   ENDFOR
      *@StopTrace M(BEFOREFOR)
     C                   SETON                                        LR