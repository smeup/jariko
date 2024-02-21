     V* ==============================================================
     D* 21/02/24  PROC WITH STATIC KEYWORD
     V* ==============================================================

     DPRCSTAT          PR
     DP                               2  0
     DQ                               2  0
     DR                               2  0
      *
     C                   Z-ADD     11            A                 2 0
     C                   Z-ADD     22            B                 2 0
     C                   Z-ADD     *zeros        C                 2 0
     C                   CALLP     PRCSTAT(A:B:C)
     C     C             DSPLY
     C                   CALLP     PRCSTAT(A:B:C)
     C     C             DSPLY
     C                   SETON                                        LR
      *
     PPRCSTAT          B
     DPRCSTAT          PI
     DP                               2  0
     DQ                               2  0
     DR                               2  0
      *
     DVSTAT            S              2  0       STATIC
      *
     C                   EVAL      VSTAT=VSTAT+1
     C                   EVAL      R=P+Q+VSTAT
     PPPRCST           E