     V* ==============================================================
     V* 15/01/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program is used by 'MUDRNRAPU00190' for its purpose.
     V* ==============================================================
     D ML            E DS                  EXTNAME(MULANGTL) INZ
     D MLL             DS                  LIKEDS(ML)

     D DS0002          S                   LIKE(ML)
      *
     C     MLSYST        DSPLY
     C     MLL.MLSYST    DSPLY

     C                   EVAL      MLL=ML

     C     MLL.MLSYST    DSPLY

     C                   SETON                                          LR

     C     *INZSR        BEGSR
     C     *ENTRY        PLIST
     C     ML            PARM      ML            DS0002
     C                   ENDSR