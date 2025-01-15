     V* ==============================================================
     V* 15/01/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Reading from a field of DS with dot notation.
    O * This DS have the same fields of another.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Dot notation is ignored and the field reference is wrong, to
    O *  another DS.
     V* ==============================================================
     D ML            E DS                  EXTNAME(MULANGTL) INZ
     D MLL             DS                  LIKEDS(ML)
      *
     C                   EVAL      MLSYST='IBMI'

     C     MLSYST        DSPLY
     C     MLL.MLSYST    DSPLY

     C                   EVAL      MLL=ML

     C     MLL.MLSYST    DSPLY

     C                   SETON                                          LR