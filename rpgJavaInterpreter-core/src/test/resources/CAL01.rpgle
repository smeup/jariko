     D NBR             S              8  0
     D MSG             S             50
     C                   CLEAR                   NBR
      * Called pgm name with trailing spaces must not be a problem
     C                   CALL      'CAL02 '
     C                   PARM                    NBR
     C                   EVAL      MSG = %CHAR(NBR)
     C     MSG           DSPLY
     C                   SETON                                        LR
      *--------------------------------------------------------------*
