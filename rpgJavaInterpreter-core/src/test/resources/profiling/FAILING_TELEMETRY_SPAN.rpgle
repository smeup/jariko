     V* ==============================================================
     V* 10/06/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Emit RPG Traces
     V* ==============================================================
     DA                S              8  0 INZ(5)
     DB                S              8  0 INZ(8)
     D RESULT          S              8  0 INZ(0)
     D COUNT           S              3  0
      *
  PROF* SPANSTART _SPANID1 "COMMENT"
     C                   EVAL      RESULT = A + B
  PROF* SPANSTART _SPANID2 "COMMENT 2"
     C                   FOR       COUNT = 10 DOWNTO 1
  PROF* SPANSTART _SPANID3
     C                   EVAL      RESULT = A + B * 8
  PROF* SPANEND
     C                   EVAL      RESULT = 0
     C                   ENDFOR
  PROF* SPANEND
  PROF* SPANEND
     C                   SETON                                        LR
  PROF* SPANSTART _SPANID4