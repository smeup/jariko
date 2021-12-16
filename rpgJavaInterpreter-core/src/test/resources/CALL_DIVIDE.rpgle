      *---------------------------------------------------------------
      * TESTED FEATURES: CALL STMT ERROR HANDLING
      * NOTES: A, B, CATCHERR ARE PASSED FROM TEST UNIT IN ORDER TO MAKE
      *        DYNAMIC THESE TESTS
      *---------------------------------------------------------------
      *
     D MSG             S             50
     D A               S              6  2
     D B               S              6  2
     D CATCHERR        S              1  0
     D RESULT          S              6  2
      *---------------------------------------------------------------
     C                   EVAL      MSG = 'CALLING DIVIDE'
     C     MSG           DSPLY
     C                   IF        CATCHERR = 0
     C                   CALL      'DIVIDE'
     C                   PARM                    A
     C                   PARM                    B
     C                   PARM                    RESULT
     C                   ELSE
      *---------------------------------------------------------------
      * ON BRANCH ELSE EXECUTION I AM SURE THAT DIVIDE WILL ARISE
      * AN ERROR FOR THIS REASON THE MUTE CHECKS ON INDICATOR
      *---------------------------------------------------------------
    MU* VAL1(*IN37) VAL2(*ON) COMP(EQ)
     C                   CALL      'DIVIDE'                             37
     C                   PARM                    A
     C                   PARM                    B
     C                   PARM                    RESULT
     C                   ENDIF
     C                   EVAL      MSG = 'DIVIDE CALLED'
     C     MSG           DSPLY
      *---------------------------------------------------------------
     C     *ENTRY        PLIST
     C                   PARM                    A
     C                   PARM                    B
     C                   PARM                    CATCHERR
     C                   PARM                    RESULT
     C                   SETON                                          LR