     V* ==============================================================
     V* 10/12/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Comparison between *HIVAL, at left side, and String
    O *  by using "not equal" operator.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * The expression is not evaluated when it's true.
     V* ==============================================================
     D$$AZIE           S              2    DIM(100) INZ('FF')

     C                   IF        *HIVAL<>$$AZIE(1)
     C     'TRUE'        DSPLY
     C                   ENDIF
     C     'END'         DSPLY

     C                   SETON                                          LR