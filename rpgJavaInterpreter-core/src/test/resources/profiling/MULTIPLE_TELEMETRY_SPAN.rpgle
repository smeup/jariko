     V* ==============================================================
     V* 26/05/2025 APU002 Creation
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
      * @StartTrace M(_SPANID1) "COMMENT"
     C                   EVAL      RESULT = A + B
      * @StartTrace M(_SPANID2) "COMMENT 2"
     C                   FOR       COUNT = 10 DOWNTO 1
      * @StartTrace M(_SPANID3)
     C                   EVAL      RESULT = A + B * 8
      * @StopTrace M(_SPANID3)
     C                   EVAL      RESULT = 0
     C                   ENDFOR
      * @StopTrace M(_SPANID2)
      * @StopTrace M(_SPANID1)
     C                   SETON                                        LR