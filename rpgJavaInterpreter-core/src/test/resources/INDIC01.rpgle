      *
      * WORKING WITH INDICATOR 15 AND EXPRESSIONS
      *

     D $IND            S              2  0
     D $I              S              2  0
     D MSG             S             10           VARYING

     C                   EVAL      $IND=15
     C                   EVAL      $I=1

      * WITH SINGLE PARENTHESIS
      * Turn off the indicator and display the value
     C                   SETOFF                                           15
     C                   EVAL      MSG=%CHAR(*IN($IND))
      * Expected: 0
     C     MSG           DSPLY
      * Turn on the indicator and display the value
     C                   SETON                                            15
     C                   EVAL      MSG=%CHAR(*IN($IND))
      * Expected: 1
     C     MSG           DSPLY


      * WITH DOUBLE PARENTHESIS
      * Turn off the indicator and display the value
     C                   SETOFF                                           15
     C                   EVAL      MSG=%CHAR(*IN(($IND)))
      * Expected: 0
     C     MSG           DSPLY
      * Turn on the indicator and display the value
     C                   SETON                                            15
     C                   EVAL      MSG=%CHAR(*IN(($IND)))
      * Expected: 1
     C     MSG           DSPLY


      * WITH ASSIGNMENT
      * Turn off the indicator and display the value
     C                   SETOFF                                           15
     C                   EVAL      MSG=%CHAR(*IN($IND))
      * Expected: 0
     C     MSG           DSPLY
      * Turn on the indicator and display the value
     C                   EVAL      *IN($IND)=*ON
     C                   EVAL      MSG=%CHAR(*IN($IND))
      * Expected: 1
     C     MSG           DSPLY


      * WITH ASSIGNMENT AND DOUBLE PARENTHESIS
      * Turn off the indicator and display the value
     C                   SETOFF                                           15
     C                   EVAL      MSG=%CHAR(*IN(($IND)))
      * Expected: 0
     C     MSG           DSPLY
      * Turn on the indicator and display the value
     C                   EVAL      *IN(($IND))=*ON
     C                   EVAL      MSG=%CHAR(*IN(($IND)))
      * Expected: 1
     C     MSG           DSPLY


      * WITH ASSIGNMENT NUMERIC EXPRESSION AFTER VARIABLE
      * Turn off the indicator and display the value
     C                   SETOFF                                           15
     C                   EVAL      MSG=%CHAR(*IN($IND+1-1))
      * Expected: 0
     C     MSG           DSPLY
      * Turn on the indicator and display the value
     C                   EVAL      *IN($IND+1-1)=*ON
     C                   EVAL      MSG=%CHAR(*IN($IND+1-1))
      * Expected: 1
     C     MSG           DSPLY


      * WITH ASSIGNMENT NUMERIC EXPRESSION BEFORE VARIABLE
      * Turn off the indicator and display the value
     C                   SETOFF                                           15
     C                   EVAL      MSG=%CHAR(*IN(1-1+$IND))
      * Expected: 0
     C     MSG           DSPLY
      * Turn on the indicator and display the value
     C                   EVAL      *IN(1-1+$IND)=*ON
     C                   EVAL      MSG=%CHAR(*IN(1-1+$IND))
      * Expected: 1
     C     MSG           DSPLY


      * WITH ASSIGNMENT VARIABLES EXPRESSION, AFTER $IND
      * Turn off the indicator and display the value
     C                   SETOFF                                           15
     C                   EVAL      MSG=%CHAR(*IN($IND+$I-$I))
      * Expected: 0
     C     MSG           DSPLY
      * Turn on the indicator and display the value
     C                   EVAL      *IN($IND+$I-$I)=*ON
     C                   EVAL      MSG=%CHAR(*IN($IND+$I-$I))
      * Expected: 1
     C     MSG           DSPLY


      * WITH ASSIGNMENT VARIABLES EXPRESSION, BEFORE $IND
      * Turn off the indicator and display the value
     C                   SETOFF                                           15
     C                   EVAL      MSG=%CHAR(*IN($I-$I+$IND))
      * Expected: 0
     C     MSG           DSPLY
      * Turn on the indicator and display the value
     C                   EVAL      *IN($I-$I+$IND)=*ON
     C                   EVAL      MSG=%CHAR(*IN($I-$I+$IND))
      * Expected: 1
     C     MSG           DSPLY


      * WITH ASSIGNMENT MISC EXPRESSION, AFTER $IND
      * Turn off the indicator and display the value
     C                   SETOFF                                           15
     C                   EVAL      MSG=%CHAR(*IN($IND+1-$I))
      * Expected: 0
     C     MSG           DSPLY
      * Turn on the indicator and display the value
     C                   EVAL      *IN($IND+1-$I)=*ON
     C                   EVAL      MSG=%CHAR(*IN($IND+1-$I))
      * Expected: 1
     C     MSG           DSPLY


      * WITH ASSIGNMENT MISC EXPRESSION, BEFORE $IND
      * Turn off the indicator and display the value
     C                   SETOFF                                           15
     C                   EVAL      MSG=%CHAR(*IN($I-1+$IND))
      * Expected: 0
     C     MSG           DSPLY
      * Turn on the indicator and display the value
     C                   EVAL      *IN($I-1+$IND)=*ON
     C                   EVAL      MSG=%CHAR(*IN($I-1+$IND))
      * Expected: 1
     C     MSG           DSPLY