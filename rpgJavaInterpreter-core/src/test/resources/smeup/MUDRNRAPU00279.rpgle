     V* ==============================================================
     V* 11/12/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * SCAN followed by %FOUND
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, jariko did not consider SCAN accountable for %FOUND
     V* ==============================================================
     D RESULT          S              1  0
     D BASE            S              6    INZ('XCABCD')

     C     'ABC'         SCAN      BASE          RESULT
     C     RESULT        DSPLY
     C                   IF        %FOUND
     C     'FOUND'       DSPLY
     C                   ELSE
     C     'NOT FOUND'   DSPLY
     C                   ENDIF

     C     'FOO'         SCAN      BASE          RESULT
     C     RESULT        DSPLY
     C                   IF        %FOUND
     C     'FOUND'       DSPLY
     C                   ELSE
     C     'NOT FOUND'   DSPLY
     C                   ENDIF

     C                   SETON                                        RT