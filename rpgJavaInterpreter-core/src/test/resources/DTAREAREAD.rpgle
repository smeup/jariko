     V* ==============================================================
     V* Data Area Read Test
     V* ==============================================================
     D TARGET          S             50A
     D DATAAREA        S             50A   INZ('TEST_AREA')
     C     *LOCK         IN        DATAAREA      TARGET            50
     C     TARGET        DSPLY
     C                   SETON                                          LR