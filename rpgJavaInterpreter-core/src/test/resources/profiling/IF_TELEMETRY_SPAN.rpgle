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
  PROF* SPANSTART BEFORESTMT
     C                   IF        1=1
  PROF* SPANSTART IFBODY
     C                   EVAL      RESULT = A + B * 8
     C                   EVAL      COUNT = COUNT + 1
  PROF* SPANEND
     C                   ELSEIF        1=2
  PROF* SPANSTART ELIFBODY
     C                   EVAL      RESULT = A + B * 8
     C                   EVAL      COUNT = COUNT + 1
  PROF* SPANEND
     C                   ELSE
  PROF* SPANSTART ELSEBODY
     C                   EVAL      RESULT = A + B * 8
     C                   EVAL      COUNT = COUNT + 1
  PROF* SPANEND
     C                   ENDIF
  PROF* SPANEND
     C                   SETON                                        LR