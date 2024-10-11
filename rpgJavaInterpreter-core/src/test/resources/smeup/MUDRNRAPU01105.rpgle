     V* ==============================================================
     V* 11/10/2024 APU011 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Allows for the correct handling of composed (nested) statements during execution,
    O * ensuring that TagStmts can be found even within complex structures.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *   Internal Server Error
    O *   Cannot invoke "String.length()" because "str" is null
     V* ==============================================================

     D  FLAG           S             10  0 INZ(0)
     C                   EXSR      SIN_SR
     C                   SETON                                        LR

     C     SIN_SR        BEGSR
     C                   EXSR      SIN_GR
     C                   ENDSR

     C     SIN_GR        BEGSR
     C                   EXSR      MEM_DT
     C                   ENDSR

     C     MEM_DT        BEGSR
     C                   CLEAR                   $GA              15
     C                   EVAL      *IN38=*OFF

     C     1             IFEQ      1
     C     RILALTRO1     TAG
     C                   ENDIF
     C     FLAG          IFEQ      0
     C     RILALTRO      TAG
     C     'FFALSE'      DSPLY
     C                   EVAL      $GA=' '
     C                   EVAL      FLAG=1
     C                   ELSE
     C     'FTRUE'       DSPLY
     C                   EVAL      $GA='FOO'
     C                   ENDIF
     C  N38              IF        $GA=' '
     C     'FGA'         DSPLY
     C                   GOTO      RILALTRO1
     C                   ENDIF
     C                   ENDSR