     V* ==============================================================
     V* 10/12/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Comparison between:
    O * - *HIVAL and String,
    O * - *HIVAL and *HIVAL
    O * by using "lower" operator.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * The expression is not evaluated when it's true.
     V* ==============================================================
     D ARR01           S              2    DIM(10) INZ('FF')
     D ARR02           S              2    DIM(10) INZ(*HIVAL)

     C                   IF        *HIVAL<ARR01(1)
     C     'TRUE'        DSPLY
     C                   ENDIF
     C     'END'         DSPLY

     C                   IF        ARR01(1)>*HIVAL
     C     'TRUE'        DSPLY
     C                   ENDIF
     C     'END'         DSPLY

     C                   IF        *HIVAL<ARR02(1)
     C     'TRUE'        DSPLY
     C                   ENDIF
     C     'END'         DSPLY

     C                   IF        ARR02(1)>*HIVAL
     C     'TRUE'        DSPLY
     C                   ENDIF
     C     'END'         DSPLY

     C                   SETON                                          LR