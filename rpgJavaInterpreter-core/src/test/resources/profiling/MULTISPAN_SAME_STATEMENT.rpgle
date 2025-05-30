     V* ==============================================================
     V* 30/05/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Emit RPG Traces
     V* ==============================================================
     DA                S              8  0 INZ(5)
     DB                S              8  0 INZ(8)
     D RESULT          S              8  0 INZ(0)
      *
  PROF* SPANSTART _SPANID1 "COMMENT"
  PROF* SPANSTART _SPANID2 "COMMENT"
  PROF* SPANSTART _SPANID3 "COMMENT"
  PROF* SPANSTART _SPANID4 "COMMENT"
     C                   EVAL      RESULT = A + B
  PROF* SPANEND
  PROF* SPANEND
  PROF* SPANEND
  PROF* SPANEND
     C                   SETON                                        LR