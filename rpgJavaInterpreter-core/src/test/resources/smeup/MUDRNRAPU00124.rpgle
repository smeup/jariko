     V* ==============================================================
     V* 30/09/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Executes DOUEQ statement by checking indicator state.
     V* ==============================================================
     D  SUM            S              2  0 INZ(0)
     D  COUNT          S              2  0 INZ(1)

     C                   EVAL      *IN35=*ON
     C                   EXSR      CALCULATE
     C     SUM           DSPLY

     C                   EVAL      *IN35=*OFF
     C                   EXSR      CALCULATE
     C     SUM           DSPLY

     C                   SETON                                          LR


     C     CALCULATE     BEGSR
     C   35COUNT         DO        10                                           # Semantic error: in this point Jariko ignored *IN35 state.
     C                   EVAL      SUM=SUM+1
     C                   ENDDO
     C                   ENDSR