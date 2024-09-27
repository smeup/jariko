     V* ==============================================================
     V* 27/09/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Executes IF statement by checking indicator state.
     V* ==============================================================
     D  MSG            S             15    INZ('') VARYING

     C                   EVAL      *IN35=*ON

     C   351             IFEQ      1
     C                   EVAL      MSG='Hello there'
     C                   EVAL      *IN35=*OFF
     C                   ELSE
     C                   EVAL      *IN35=*ON
     C                   ENDIF

     C   351             IFEQ      1                                            # Semantic error: in this point Jariko ignored *IN35 state.
     C                   EVAL      MSG='Hello friend'
     C                   ELSE
     C                   EVAL      MSG='Hello guys'
     C                   ENDIF

     C     MSG           DSPLY

     C                   SETON                                          LR