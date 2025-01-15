     V* ==============================================================
     V* 15/01/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Writing to a field of DS without specify the DS. There are
    O *  two DS with same fields.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * On AS400 is considered ONLY the first DS declared instead
    O *  all DS with same field as done on Jariko.
     V* ==============================================================
     D PGM_NAME        S             17    INZ('MUDRNRAPU00190_P1')
     D MULANGDS      E DS                  EXTNAME(MULANGTL) INZ

     C                   EVAL      MLSYST='IBMI'
     C                   CALL      PGM_NAME
     C                   PARM                    MULANGDS

     C                   SETON                                          LR