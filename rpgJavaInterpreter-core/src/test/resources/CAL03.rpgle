      * On AS400 this program fails with error "RNX8888 - CAL03 was called recursively "
     D P1              S              1
     D MSG             S             50
      *--------------------------------------------------------------*
     C     *ENTRY        PLIST
     C                   PARM                    P1
      *--------------------------------------------------------------*
     C                   IF        P1 <> 'R'
     C                   EVAL      P1 = 'R'
     C                   EVAL      MSG = 'HELLO'
     C                   CALL      'CAL03'
     C                   PARM                    P1
     C                   ENDIF
      *--------------------------------------------------------------*
     C     MSG           DSPLY
      *--------------------------------------------------------------*
     C                   SETON                                        LR
