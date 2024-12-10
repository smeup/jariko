     V* ==============================================================
     V* 10/12/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Comparison between *HIVAL and String by using "lower/equal"
    O *  operator for `*HIVAL`.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * The expression is not evaluated when it's true.
     V* ==============================================================
     D$$AZIE           S              2    DIM(100) INZ('FF')

     C                   IF        *HIVAL<=$$AZIE(1)
     C     'TRUE'        DSPLY
     C                   ENDIF
     C     'END'         DSPLY

     C                   IF        $$AZIE(1)>=*HIVAL
     C     'TRUE'        DSPLY
     C                   ENDIF
     C     'END'         DSPLY

     C                   SETON                                          LR