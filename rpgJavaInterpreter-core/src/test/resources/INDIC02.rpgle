      *
      * WORKING WITH INDICATOR 15 AND DIRECT ACCESS
      *

     D MSG             S             10           VARYING

      * WITHOUT PARENTHESIS
      * Turn off the indicator and display the value
     C                   SETOFF                                           15
     C                   EVAL      MSG=%CHAR(*IN15)
      * Expected: 0
     C     MSG           DSPLY
      * Turn off the indicator and display the value
     C                   SETON                                            15
     C                   EVAL      MSG=%CHAR(*IN15)
      * Expected: 1
     C     MSG           DSPLY


      * WITH PARENTHESIS
      * Turn off the indicator and display the value
     C                   SETOFF                                           15
     C                   EVAL      MSG=%CHAR(*IN(15))
      * Expected: 0
     C     MSG           DSPLY
      * Turn off the indicator and display the value
     C                   SETON                                            15
     C                   EVAL      MSG=%CHAR(*IN(15))
      * Expected: 1
     C     MSG           DSPLY


      * WITH DOUBLE PARENTHESIS
      * Turn off the indicator and display the value
     C                   SETOFF                                           15
     C                   EVAL      MSG=%CHAR(*IN((15)))
      * Expected: 0
     C     MSG           DSPLY
      * Turn off the indicator and display the value
     C                   SETON                                            15
     C                   EVAL      MSG=%CHAR(*IN((15)))
      * Expected: 1
     C     MSG           DSPLY


      * WITHOUT PARENTHESIS AND ASSIGNMENT
      * Turn off the indicator and display the value
     C                   SETOFF                                           15
     C                   EVAL      MSG=%CHAR(*IN15)
      * Expected: 0
     C     MSG           DSPLY
      * Turn off the indicator and display the value
     C                   EVAL      *IN15=*ON
     C                   EVAL      MSG=%CHAR(*IN15)
      * Expected: 1
     C     MSG           DSPLY


      * WITH PARENTHESIS AND ASSIGNMENT
      * Turn off the indicator and display the value
     C                   SETOFF                                           15
     C                   EVAL      MSG=%CHAR(*IN(15))
      * Expected: 0
     C     MSG           DSPLY
      * Turn off the indicator and display the value
     C                   EVAL      *IN(15)=*ON
     C                   EVAL      MSG=%CHAR(*IN(15))
      * Expected: 1
     C     MSG           DSPLY


      * WITH DOUBLE PARENTHESIS AND ASSIGNMENT
      * Turn off the indicator and display the value
     C                   SETOFF                                           15
     C                   EVAL      MSG=%CHAR(*IN((15)))
      * Expected: 0
     C     MSG           DSPLY
      * Turn off the indicator and display the value
     C                   EVAL      *IN((15))=*ON
     C                   EVAL      MSG=%CHAR(*IN((15)))
      * Expected: 1
     C     MSG           DSPLY