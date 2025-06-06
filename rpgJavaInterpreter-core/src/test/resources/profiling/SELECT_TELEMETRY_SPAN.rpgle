     V* ==============================================================
     V* 05/06/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Emit RPG Traces
     V* ==============================================================
     DA                S              8  0 INZ(5)
     DB                S              8  0 INZ(8)
     D RESULT          S              8  0 INZ(0)
     D COUNT           S              3  0
      *
  PROF* SPANSTART BEFORESELECT
     C                   SELECT
     C                   WHEN      COUNT=1
  PROF* SPANSTART CONTENT1
     C                   EVAL      RESULT = A + B * 1
  PROF* SPANEND
     C                   WHEN      COUNT=2
  PROF* SPANSTART CONTENT2
     C                   EVAL      RESULT = A + B * 2
  PROF* SPANEND
     C                   OTHER
  PROF* SPANSTART CONTENT3
     C                   EVAL      RESULT = A + B * 3
  PROF* SPANEND
     C                   ENDSL
  PROF* SPANEND
     C                   SETON                                        LR