     V* ==============================================================
     V* 10/12/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Comparison between:
    O * - *HIVAL as Standalone of 2 chars,
    O * - *HIVAL as Standalone of 4 chars,
    O * by using "equal" operator.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * The expression is not evaluated when it's true.
     V* ==============================================================
     D D1              S              2    INZ(*HIVAL)
     D D2              S              4    INZ(*HIVAL)

     C                   IF        D1=D2
     C     'TRUE'        DSPLY
     C                   ENDIF
     C     'END'         DSPLY

     C                   SETON                                          LR