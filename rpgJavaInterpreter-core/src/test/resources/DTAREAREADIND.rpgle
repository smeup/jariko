     V* ==============================================================
     V* Data Area Read with Indicator Test
     V* ==============================================================
     D SCAATTDS        DS           460
     D IND             S             20
     C     *DTAARA       DEFINE    C£C£E00D      SCAATTDS
     C                   EVAL      SCAATTDS='CURRENT'
     C                   IN        SCAATTDS                             50
     C                   EVAL      IND=*IN50
     C     SCAATTDS      DSPLY
     C     IND           DSPLY
     C                   SETON                                          LR