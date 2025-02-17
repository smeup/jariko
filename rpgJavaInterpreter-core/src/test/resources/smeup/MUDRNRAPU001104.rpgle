     V* ==============================================================
     V* 17/02/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Comparison between:
    O * - *HIVAL and number with 2 digits,
    O * - *HIVAL and *HIVAL
    O * by using "not equal" operator.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * The expression is not evaluated correctly for third and
    O *  fourth comparison: must be false.
     V* ==============================================================
     D NOHIVAL         S              2  0 INZ(98)
     D HIVAL           S              2  0 INZ(*HIVAL)

     C                   IF        *HIVAL<>NOHIVAL
     C     'TRUE 1'      DSPLY
     C                   ENDIF
     C     'END 1'       DSPLY

     C                   IF        NOHIVAL<>*HIVAL
     C     'TRUE 2'      DSPLY
     C                   ENDIF
     C     'END 2'       DSPLY

     C                   IF        *HIVAL<>HIVAL
     C     'TRUE 3'      DSPLY
     C                   ENDIF
     C     'END 3'       DSPLY

     C                   IF        HIVAL<>*HIVAL
     C     'TRUE 4'      DSPLY
     C                   ENDIF
     C     'END 4'       DSPLY

     C                   SETON                                          LR