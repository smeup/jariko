      * Test multiple api inclusion
      * MSG is already defined in APIVARS but does not must cause a conflict
     D  MSG            S             50
      *
      /API APIVARS
      *
     C                   EVAL      MSG = 'Hello world'
     C     MSG           DSPLY
     C                   SETON                                        LR