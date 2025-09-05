     V* ==============================================================
     V* 02/09/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Read and write data areas in subroutine
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, Jariko could not interpret data areas
     V* ==============================================================
     D SCAATTDS        DS           460
     C     SCRDSSCA      BEGSR
     C     *DTAARA       DEFINE    C£C£E00D      SCAATTDS
     C     *LOCK         IN        SCAATTDS
     C                   OUT       SCAATTDS
     C                   ENDSR
     C                   EXSR      SCRDSSCA
     C                   SETON                                          LR
