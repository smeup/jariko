     D MYDS            DS                  QUALIFIED
     D  FLD1                          5  1
     D  FLD2                         10
      *---------------------------------------------------------------
     D PROCEDURE_1     PR
     D  MYDSPROC                            LIKEDS(MYDS)
      *---------------------------------------------------------------
     D FLD1_C          S              5
     D FLD2_C          S             10
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
     C                   CALLP     PROCEDURE_1(MYDS)
     C                   EVAL      FLD1_C=%CHAR(MYDS.FLD1)
     C                   EVAL      FLD2_C=%CHAR(MYDS.FLD2)
      * Must be '10.2'
     C     FLD1_C        DSPLY
      * Must be 'ABCDE'
     C     FLD2_C        DSPLY
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
     P PROCEDURE_1     B
     D PROCEDURE_1     PI
     D  MYDSPROC                            LIKEDS(MYDS)
      *
     C                   EVAL      MYDSPROC.FLD1=5,1*2
     C                   EVAL      MYDSPROC.FLD2='ABCDE'
      *
     P PROCEDURE_1     E
      *---------------------------------------------------------------

