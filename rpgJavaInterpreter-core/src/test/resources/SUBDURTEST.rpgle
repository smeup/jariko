     D ST              S               Z
     D EN              S               Z
     D $TIMMS          S             10  0
     D MSG             S             50
     C                   eval      ST = %timestamp('2011-09-14-22.06.48.592000')
     C                   eval      EN = %timestamp('2011-09-14-22.06.49.692000')
     C     EN            SUBDUR    ST            $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
     C                   EVAL      MSG=%CHAR($TIMMS)
     C     MSG           DSPLY
     C                   SETON                                        LR
