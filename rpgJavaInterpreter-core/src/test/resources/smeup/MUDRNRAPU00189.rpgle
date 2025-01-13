     V* ==============================================================
     V* 13/01/2025 APU001 Creation
     V* 13/01/2025 BENMAR Improvements
     V* ==============================================================
    O * PROGRAM GOAL
    O * Writing on a field of DS which use `EXTNAME` of a file,
    O *  writes to file too.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Program called doesn't have new value.
     V* ==============================================================
     D PGM_NAME        S             17    INZ('MUDRNRAPU00189_P1')
     D MULANGDS      E DS                  EXTNAME(MULANGTL) INZ

     C                   EVAL      MLSYST='IBMI'
     C                   CALL      PGM_NAME
     C                   PARM                    MULANGDS

     C                   SETON                                          LR