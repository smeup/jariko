     V* ==============================================================
     V* Data Area Read Test
     V* ==============================================================
     D SCAATTDS        DS           460
     C     *DTAARA       DEFINE    C£C£E00D      SCAATTDS
     C                   EVAL      SCAATTDS='CURRENT'
     C     *LOCK         IN        SCAATTDS
     C     SCAATTDS      DSPLY
     C                   SETON                                          LR