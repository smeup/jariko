     V* ==============================================================
     V* 11/10/2024 APU011 Creation
     V* 15/10/2024 APU001 Improvements
     V* ==============================================================
    O * PROGRAM GOAL
    O * Allows for the correct handling of composed (nested)
    O *  statements during execution, ensuring that `TagStmts`
    O *  can be found even within complex structures.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *   Internal Server Error
    O *   Cannot invoke "String.length()" because "str" is null
     V* ==============================================================
     D  FLAG           S             10  0 INZ(0)

     C                   CLEAR                   STR              15
     C                   EVAL      *IN38=*OFF

     C     1             IFEQ      1
     C     JUMP1         TAG
     C                   ENDIF
     C     FLAG          IFEQ      0
     C     JUMP          TAG
     C     'FLG-FALSE'   DSPLY
     C                   EVAL      STR=' '
     C                   EVAL      FLAG=1
     C                   ELSE
     C     'FLG-TRUE'    DSPLY
     C                   EVAL      STR='FOO'
     C                   ENDIF
     C  N38              IF        STR=' '
     C     'EMPTY'       DSPLY
     C                   GOTO      JUMP1                                        #Cannot invoke "String.length()" because "str" is null
     C                   ENDIF