     V* ==============================================================
     V* Data Area Read with Indicator Test
     V* ==============================================================
     D TARGET          S             50A
     D DATAAREA        S             50A   INZ('TEST_AREA')
     D IND             S             20
     C                   IN        DATAAREA      TARGET            50
     C                   EVAL      IND=*IN50
     C     TARGET        DSPLY
     C     IND           DSPLY
     C                   SETON                                          LR