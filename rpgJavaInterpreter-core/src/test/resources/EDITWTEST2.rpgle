     V*=====================================================================
     V*  Tests for %EDITW
     V*=====================================================================
     D*----------------------------------------------------------------
     D DSP             S             50
      *
     D  N              S              6  2
     D  D6             S              6  0
     D  NNN070         S              7  0
     D  NNN051         S              5  1
      *
     C*----------------------------------------------------------------
     C                   EVAL      D6=082345
     C                   EVAL      DSP='x' + %EDITW(D6:'0  :  :  ')
     C     DSP           DSPLY
     C*----------------------------------------------------------------
     C                   EVAL      NNN070=2345
     C                   EVAL      DSP='x' + %EDITW(NNN070:'    .   -')
     C     DSP           DSPLY
     C*----------------------------------------------------------------
     C                   EVAL      NNN070=-2345
     C                   EVAL      DSP='x' + %EDITW(NNN070:'    .   -')
     C     DSP           DSPLY
     C*----------------------------------------------------------------
     C                   EVAL      NNN051=-21,4
     C                   EVAL      DSP='x' + %EDITW(NNN051:'   0, -%')
     C     DSP           DSPLY
     C*----------------------------------------------------------------
     C                   SETON                                        LR