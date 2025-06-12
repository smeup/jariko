     V* ==============================================================
     V* 04/06/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Emit RPG Traces
     V* ==============================================================
     DA                S              8  0 INZ(5)
     DB                S              8  0 INZ(8)
     D RESULT          S              8  0 INZ(0)
     D COUNT           S              3  0
      *
  PROF* SPANSTART BEFOREDO
     C                   DO        100
  PROF* SPANSTART DOBODYSTART
     C                   EVAL      RESULT = A + B * 8
  PROF* SPANEND
     C                   ENDDO
  PROF* SPANEND
     C                   SETON                                        LR