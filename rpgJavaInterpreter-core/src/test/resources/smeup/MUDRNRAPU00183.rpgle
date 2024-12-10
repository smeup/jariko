     V* ==============================================================
     V* 10/12/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Comparison between *HIVAL and String by using "not equal"
    O *  operator.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * The expression is not evaluated when it's true.
     V* ==============================================================
     D ARR01           S              2    DIM(10) INZ('FF')

     C                   IF        *HIVAL<>ARR01(1)
     C     'TRUE'        DSPLY
     C                   ENDIF
     C     'END'         DSPLY

     C                   IF        ARR01(1)<>*HIVAL
     C     'TRUE'        DSPLY
     C                   ENDIF
     C     'END'         DSPLY

     C                   SETON                                          LR