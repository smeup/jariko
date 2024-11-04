     V* ==============================================================
     V* 07/10/2024 APU011 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Verifies the sorting of values in an array in a data structure
    O * when the factor one is a reference to an array field.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *   Issue executing SortAStmt at line 26.
    O *   An operation is not implemented: take not yet implemented for ProjectedArrayValue
     V* ==============================================================

     D INDX            S              2  0
     D MSG             S              5
     D                 DS
     DARR                            10  0 DIM(5)

     C                   EVAL      ARR(1)=3
     C                   EVAL      ARR(2)=2
     C                   EVAL      ARR(3)=4
     C                   EVAL      ARR(4)=1
     C                   EVAL      ARR(5)=5
     C     'ORIGINAL'    DSPLY
     C                   EXSR      SHOW_RES
     C                   SORTA     %SUBARR(ARR:2:4)
     C     'ORDERED'     DSPLY
     C                   EXSR      SHOW_RES

     C                   SETON                                          LR

     C     SHOW_RES      BEGSR
     C                   FOR       INDX=1 TO %ELEM(ARR)
     C                   EVAL      MSG=%CHAR(ARR(INDX))
     C     MSG           DSPLY
     C                   ENDFOR
     C                   ENDSR