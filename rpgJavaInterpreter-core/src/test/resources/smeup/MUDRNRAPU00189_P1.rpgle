     V* ==============================================================
     V* 13/01/2025 APU001 Creation
     V* 13/01/2025 BENMAR Improvements
     V* 15/01/2025 APU001 Clean code for more readable.
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program is used by 'MUDRNRAPU00189' for its purpose.
     V* ==============================================================
     FMULANGTL  IF   E           K DISK
     D ML            E DS                  EXTNAME(MULANGTL) INZ
     D MLL             DS                  LIKEDS(ML)

     D DS0002          S                   LIKE(ML)
      *
     C     MLSYST        DSPLY
     C     MLPROG        DSPLY

     C     MLSYST        CHAIN     MULANGTL

     C     MLSYST        DSPLY
     C     MLPROG        DSPLY
     C     MLL.MLSYST    DSPLY
     C     MLL.MLPROG    DSPLY

     C                   EVAL      MLL=ML

     C     MLL.MLSYST    DSPLY
     C     MLL.MLPROG    DSPLY

     C                   SETON                                          LR

     C     *INZSR        BEGSR
     C     *ENTRY        PLIST
     C     ML            PARM      ML            DS0002
     C                   ENDSR