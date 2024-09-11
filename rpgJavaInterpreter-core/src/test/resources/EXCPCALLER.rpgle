      * Test declarations
     D £DBG_Str        S             2

      * When calling a program with more parameters than it expects, exceeding parameters should be ignored
     C                   CALL      'EXCPCALLEE'
     C                   PARM      *OFF          $A                1
     C                   PARM      *OFF          $B                1

      * Test output
     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY