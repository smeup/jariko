     V* ==============================================================
     V* 13/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Assignment of a boolean value to an array by using MOVEA.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix:
    O *  `An operation is not implemented: Converting BooleanValue
    O *   to ArrayType`
     V* ==============================================================
     D ARR             S              1    DIM(3) INZ(*ON)
     D I               S              1  0
     D MSG             S              1

     C                   EXSR      SHOW_RES
     C                   MOVEA     *OFF          ARR                            #An operation is not implemented: Converting BooleanValue to ArrayType
     C                   EXSR      SHOW_RES

     C                   SETON                                          LR

     C     SHOW_RES      BEGSR
     C                   FOR       I=1 TO %ELEM(ARR)
     C                   EVAL      MSG=%CHAR(ARR(I))
     C     MSG           DSPLY
     C                   ENDFOR
     C                   ENDSR