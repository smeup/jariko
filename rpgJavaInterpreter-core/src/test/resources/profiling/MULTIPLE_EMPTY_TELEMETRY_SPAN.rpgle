     V* ==============================================================
     V* 13/05/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Emit RPG Traces
     V* ==============================================================
     DA                S              8  0 INZ(5)
     DB                S              8  0 INZ(8)
     D RESULT          S              8  0 INZ(0)
      *
  PROF* SPANSTART _EMPTY
  PROF* SPANSTART _EMPTY2
  PROF* SPANEND
  PROF* SPANSTART _EMPTY3
  PROF* SPANEND
  PROF* SPANEND
  PROF* SPANSTART _SPANID1
  PROF* SPANSTART _EMPTY4
  PROF* SPANSTART _EMPTY5
  PROF* SPANSTART _EMPTY6
  PROF* SPANEND
  PROF* SPANEND
  PROF* SPANEND
  PROF* SPANSTART _EMPTY7
  PROF* SPANEND
     C                   EVAL      RESULT = A + B
  PROF* SPANSTART _EMPTY8
  PROF* SPANEND
  PROF* SPANEND
     C                   SETON                                        LR
  PROF* SPANSTART _EMPTY9
  PROF* SPANEND