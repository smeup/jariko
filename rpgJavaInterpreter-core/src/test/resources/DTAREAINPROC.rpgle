     V* ==============================================================
     V* 07/08/2025 APU002 Adapt for Jariko test runners
     V* 07/08/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Read and write data areas in procedures
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, Jariko could not interpret data areas
     V* ==============================================================
     D DS1             DS            10
     D DATAAREA1       S             50A   INZ('APU001D1')
     D ARR1            S              5    DIM(2)
     DPOPULATE_DA      PR

     C                   CALLP     POPULATE_DA()
     C                   SETON                                          LR

     PPOPULATE_DA      B
     DPOPULATE_DA      PI
     D ARRP            S              5    DIM(2)
     C     *LOCK         IN        DATAAREA1     DS1
     C                   EVAL      ARRP(1)='Foo'
     C                   EVAL      ARRP(2)='Bar'
     C                   MOVEA     ARRP          DS1
     C                   OUT       DATAAREA1     DS1
     PPOPULATE_DA      E