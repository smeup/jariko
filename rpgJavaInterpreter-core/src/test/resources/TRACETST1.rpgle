     V* ==============================================================
     V* 04/11/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Perform tracing
     V* ==============================================================
     D $A              S              2
     DCALL1            PR
     Da                               2  0
     Db                               2  0

     C                   EXSR      SR

     C     SR            BEGSR
     C     1             IFEQ      1
     C                   CALLP     CALL1(5:6)
     C                   EVAL      $A=CALL1(5:6)
     C                   EVAL      $A = '5'
     C                   CALL      'TRACETST2'
     C     0             IFGT      1
     C     $A            DSPLY
     C                   ENDIF
     C     '1'           DSPLY
     C                   ENDIF
     C                   ENDSR
     C                   SETON                                          LR

     PCALL1            B
     DCALL1            PI
     Da                               2  0
     Db                               2  0
     C                   RETURN    a+b
     PCALL1            E