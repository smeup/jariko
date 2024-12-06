     V* ==============================================================
     V* 06/12/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * IF comparison by using *HIVAL at left side.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * The expression is not evaluated when it's true.
     V* ==============================================================
     D$$AZIE           S              2    DIM(100) INZ(*HIVAL)

     C                   IF        *HIVAL=$$AZIE(1)
     C     'TRUE'        DSPLY
     C                   ENDIF
     C     'END'         DSPLY

     C                   SETON                                          LR