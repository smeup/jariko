     V* ==============================================================
     V* 29/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program tests the moving of value, as indicator,
    O *  from Factor 2 to Result. This operation is performed at the
    O *  end of execution of program called.
    O * In this case, MUDRNRAPU00172_P turn on indicator 35. Later,
    O *  `DO` block ends its execution.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix:
    O * `Issue executing MoveLStmt at line 9. Index 10 out of
    O *  bounds for length 10`
    O * Because this logic for program called didn't exist.
     V* ==============================================================
     D ARRAY           S              1  0 DIM(10) INZ(0)
     D VALUE           S              1  0 INZ(1)
     D PGM_NAME        S             17    INZ('MUDRNRAPU00172_P' )

     C     1             DO        11            CNT               5 0
     C                   EXSR      PGM_CALL
     C     V35           DSPLY
     C   35              LEAVE
     C                   MOVEL(P)  VALUE         ARRAY(CNT)                     #Issue executing MoveLStmt at line 9. Index 10 out of bounds for length 10
     C     'LOOP'        DSPLY
     C                   ENDDO

     C                   SETON                                          LR



     C     PGM_CALL      BEGSR
     C                   SETOFF                                       35
     C                   CALL      PGM_NAME
     C     *IN35         PARM                    V35               1
     C                   ENDSR