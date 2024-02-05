     D $IND            S              2  0
     D MSG             S             10           VARYING

     C                   EVAL      $IND=15

      * Turn off the indicator 15 and display the value of the indicator
     C                   SETOFF                                           15
     C                   EVAL      MSG=%CHAR(*IN($IND))
      * Expected: 0
     C     MSG           DSPLY
      * Turn on the indicator 15 and display the value of the indicator
     C                   SETON                                            15
     C                   EVAL      MSG=%CHAR(*IN($IND))
      * Expected: 1
     C     MSG           DSPLY