     V* ==============================================================
     V* 30/09/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Executes FOR statement by checking indicator state.
     V* ==============================================================
     D  SUM            S              2  0 INZ(0)
     D  ARRAY          S              1  0 DIM(10) INZ(1)
     D  INDX           S              2  0 INZ(0)

     C                   EVAL      *IN35=*ON
     C                   EXSR      CALCULATE
     C     SUM           DSPLY

     C                   EVAL      *IN35=*OFF
     C                   EXSR      CALCULATE
     C     SUM           DSPLY

     C                   SETON                                          LR


     C     CALCULATE     BEGSR
     C   35              FOR       INDX=1 TO %ELEM(ARRAY)                       # Semantic error: in this point Jariko ignored *IN35 state.
     C                   EVAL      SUM=SUM+ARRAY(INDX)
     C                   ENDFOR
     C                   ENDSR