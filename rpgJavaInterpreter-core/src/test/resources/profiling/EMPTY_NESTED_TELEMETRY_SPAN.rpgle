     V* ==============================================================
     V* 12/05/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Emit RPG Traces
     V* ==============================================================
     DA                S              8  0 INZ(5)
     DB                S              8  0 INZ(8)
     D RESULT          S              8  0 INZ(0)
      *
  PROF* SPANSTART _EMPTY "COMMENT"
  PROF* SPANSTART _EMPTY2 "COMMENT"
  PROF* SPANSTART _EMPTY3 "COMMENT"
  PROF* SPANEND
  PROF* SPANEND
  PROF* SPANEND
  PROF* SPANSTART _SPANID1 "COMMENT"
     C                   EVAL      RESULT = A + B
  PROF* SPANEND
     C                   SETON                                        LR