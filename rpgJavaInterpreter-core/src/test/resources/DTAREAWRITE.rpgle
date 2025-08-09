     V* ==============================================================
     V* Data Area Write Test  
     V* ==============================================================
     D SOURCE          S             50A   INZ('WRITTEN_VALUE')
     D DATAAREA        S             50A   INZ('TEST_AREA')
     C                   OUT       DATAAREA      SOURCE            50
     C     SOURCE        DSPLY
     C                   SETON                                          LR